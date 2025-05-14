The JVM tries to find a suitable exception handler in the call stack to handle the exception.

When an exception is thrown:
------------------------------------------------------------------
The JVM creates an Exception object in Heap Memory & hands it to the Java Runtime (JVM)
Stack trace is created using the call stack,catch block handles it. 

If not handled:
---------------------------------------------------------------------
JVM searches the call stack for a suitable catch block.
If no handler is found, JVM terminates the program.

Throwable (parent of all)
├── Error
└── Exception
    └── RuntimeException

RuntimeException and its subclasses don't require explicit handling
All other exceptions (subclasses of Exception but not RuntimeException) are checked

Checked Exceptions (Compile-Time Exceptions) -  java.io.*
---------------------------------------------------
Must be handled using try-catch or declared using throws.

IOException
SQLException
InterruptedException

try {
    FileReader file = new FileReader("non_existing_file.txt");
} catch (IOException e) {
    System.out.println("File not found");
}



Unchecked Exceptions (Runtime Exceptions)
---------------------------------------------------
Inherit from RuntimeException.
Usually caused by logical issues in the code.

NullPointerException
ArrayIndexOutOfBoundsException
ArithmeticException
ClassCastException

public static void main(String[] args) {
    String str = null;
    System.out.println(str.length());  // Causes NullPointerException
}

Errors
----------------------------------------------------
Generally caused by JVM issues or resource exhaustion(memory overflow, system crash)
Cannot be handled using try-catch (non-Recoverable)

OutOfMemoryError
StackOverflowError
VirtualMachineError

public static void main(String[] args) {
    main(args); // Causes StackOverflowError
}

try-with-resources // No need for finally block to close resources
---------------------------------------------------------------------

try (FileReader file = new FileReader("example.txt")) {
    // Code that uses the file
} catch (IOException e) {
    System.out.println("File error: " + e.getMessage());
}



Multiple Catch Blocks
-----------------------------------------------------
Order should be from specific to general exceptions
Executes even if there's a return statement in the try block

try {
    int arr[] = new int[5];
    arr[2] = 12;
    arr[10] = 50;
    return arr[2]; // 
} catch (ArithmeticException e) {
    System.out.println("Arithmetic Exception");
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Array Index Out of Bounds");
}finally {
    System.out.println("Cleanup tasks");
}

//In Java 7+

try {
    // code that might throw exceptions
} catch (IOException | SQLException e) {
    // Handle both exception types the same way
}

throw – Manually throwing an exception
------------------------------------------------------------------------
- It's used inside a method's body to signal that an exceptional condition has occurred.
- If you throw a checked exception (like IOException), and you don't handle it with 
a try-catch block, you must declare it with throws in the method's signature. Otherwise, 
you'll get a compile-time error.

throws – declares that a method might throw an exception.
-------------------------------------------------------------------------
- The throws keyword is used in a method's declaration to indicate that the method might 
throw one or more checked exceptions
- Useful for Exception Rethrowing & Chaining
- It also occurs when you are writing an override of a method that has throws in its 
signature. Overrides must match or be less broad with their throws clauses.
- It is bad practice to declare that a method throws a checked exception, when it never 
does. It makes the calling code handle exceptions that will never happen.

✅ Is it possible to throw a checked exception without declaring it in the throws clause?

No, it is not possible. If a method throws a checked exception, it must 
either handle it using a try-catch block or declare it in its throws clause.

import java.io.IOException;

class CustomCheckedException extends Exception {
    public CustomCheckedException(String message) {
        super(message);
    }
}

public class ExceptionExample {

    // Attempting to throw without declaring throws clause
    public void riskyOperation() {
        // ... some code ...
        // Attempt to throw our custom checked exception
        throw new CustomCheckedException("Something went wrong!"); // Compile-time Error here
        // ... more code ...
    }

    public static void main(String[] args) {
        ExceptionExample example = new ExceptionExample();
        example.riskyOperation();
    }
}

// The Problem: Because CustomCheckedException is a checked exception, the compiler 
// requires us to either:
// Handle it with a try-catch block within the riskyOperation() method.
// Declare that riskyOperation() throws this exception using the throws clause in the 
// method's signature.

// Corrected method - 1. handling the exception
public void riskyOperation() {
    try{
        throw new CustomCheckedException("Something went wrong!");
    } catch (CustomCheckedException e){
        System.out.println("Handled Exception: " + e.getMessage());
    }
}

// Corrected method - 2. declaring the exception.
public void riskyOperationThrows() throws CustomCheckedException{
    throw new CustomCheckedException("Something went wrong!");
}

ExceptionExample example = new ExceptionExample();
example.riskyOperation();
try{
    example.riskyOperationThrows();
} catch (CustomCheckedException e){
    System.out.println("Handled Exception from throws method: " + e.getMessage());
}


Custom Exceptions
-------------------------------------------------------------------------
Create custom exceptions by extending Exception or RuntimeException

class CustomException extends Exception {
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(String message) {
        super(message);
    }
}

public class CustomExceptionExample {

    public static void methodThatMightFail() throws Exception {
        try {
            // Simulate an operation that might throw a standard exception
            int[] arr = new int[5];
            int value = arr[10]; // This will throw ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            // Wrap the standard exception in a custom exception
            throw new CustomException("Array access failed", e);
        } catch(Exception e){
            throw new CustomException("General Exception caught",e);
        }
    }

    public static void main(String[] args) {
        try {
            methodThatMightFail();
        } catch (CustomException e) {
            // e.getMessage() -> "Array access failed"
            System.err.println("Caught CustomException: " + e.getMessage());
            if (e.getCause() != null) { // Throwable object 
                System.err.println("Original Exception: " + e.getCause());
                e.getCause().printStackTrace(); 
            }
        } catch (Exception e){
            // e.getMessage() -> "General Exception caught"
            System.err.println("Caught General Exception: " + e.getMessage());
            e.printStackTrace(); // for debugging purpose not to be used in production
        }
    }
}


NullPointerException            -> Accessing a null reference
NumberFormatException           -> Invalid string-to-number conversion
ArrayIndexOutOfBoundsException  -> Array index out of range
ClassCastException              -> Invalid type casting
IllegalArgumentException        -> Invalid method argument


Exception Propagation
-----------------------------------------------------------------
Exception propagation (also called exception bubbling) is the natural behavior of 
exceptions in Java. If a method does not handle a checked or unchecked exception, 
it is automatically passed up the call stack to the calling method.

public class ExceptionPropagationExample {

    public static void methodA() {
        int result = 10 / 0; // ArithmeticException will be thrown here
    }

    public static void methodB() {
        methodA(); // methodA does not handle the exception
    }

    public static void main(String[] args) {
        try {
            methodB(); // methodB does not handle the exception, main handles it.
        } catch (ArithmeticException e) {
            System.err.println("Main: Caught ArithmeticException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



Rethrowing exceptions:
------------------------------------------------------------------
Rethrowing an exception means catching an exception, performing some 
local handling (like logging), and then passing the same exception up the call stack.

What happens if a method overrides a superclass method that does not throw any checked 
exceptions, but the overriding method attempts to throw a checked exception
----------------------------------------------------------------------------------------
Overriding methods cannot throw checked exceptions that are not declared by the 
overridden method. However, overriding methods can throw unchecked exceptions 
(subclasses of RuntimeException

public class RethrowExample {

    public static void methodA() throws Exception {
        try {
            int result = 10 / 0; // This will throw an ArithmeticException
        } catch (ArithmeticException e) {
            System.err.println("MethodA: Caught ArithmeticException, logging it...");
            // Log the exception or perform some local handling
            System.err.println("MethodA: Error message: " + e.getMessage());
            throw e; // Rethrow the original exception
        }
    }

    public static void methodB() {
        try {
            methodA();
        } catch (Exception e) {
            System.err.println("MethodB: Caught exception from methodA: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        methodB();
    }
}

Exception chaining
-------------------------------------------------------------------
Exception chaining involves catching an exception and then throwing a new exception that 
wraps the original exception. This preserves the original exception's information while 
providing additional context.


public class ExceptionChainingExample {

    public static int calculate(int[] numbers, int index, int divisor) {
        try {
            return numbers[index] / divisor;
        } catch (ArithmeticException e) {
            throw new RuntimeException("Calculation failed due to division by zero", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Calculation failed due to invalid array index", e);
        }
    }

    public static void methodA(int[] numbers, int index, int divisor) throws RuntimeException {
        try {
            int result = calculate(numbers, index, divisor);
            System.out.println("Result: " + result);
        } catch (RuntimeException e) {
            throw new RuntimeException("Method A encountered an error", e);
        }
    }

    public static void methodB(int[] numbers, int index, int divisor) {
        try {
            methodA(numbers, index, divisor);
        } catch (RuntimeException e) {
            System.err.println("MethodB: Caught RuntimeException: " + e.getMessage());
            System.err.println("Original Exception: " + e.getCause());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int[] numbers = {10, 20, 30};
        int index = 2;
        int divisor = 0; // Trigger ArithmeticException

        methodB(numbers, index, divisor);

        System.out.println("---");

        divisor = 2;
        index = 5; // Trigger ArrayIndexOutOfBoundsException

        methodB(numbers, index, divisor);
    }
}




Best Practices
-------------------------------------------------------------------
✅ Catch specific exceptions instead of Exception.
✅ Use meaningful exception messages
✅ Close resources using finally or try-with-resources
✅ Never use exceptions for normal control flow
✅ Avoid empty catch blocks

