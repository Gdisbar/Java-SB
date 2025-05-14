

// Java code files have the extension . java. This extension indicates that the file 
// contains Java source code, which can be compiled into bytecode by the Java compiler

// Classloader: Classloader in Java is a part of the Java Runtime Environment (JRE) 
// which is used to load Java classes into the Java Virtual Machine dynamically. 
// It adds security by separating the package for the classes of the local file 
// system from those that are imported from network sources.

// Bytecode Verifier: It checks the code fragments for illegal code that can 
// violate access rights to objects.

// Security Manager: It determines what resources a class can access such as 
// reading and writing to the local disk.


// Java code is compiled into bytecode by the Java compiler at compilation time. 
// This bytecode is platform-independent and can be executed on any device with a 
// JVM, which interprets or compiles(using Just-In-Time compiler) the bytecode into 
// native machine code at runtime and produces output. Since we can carry this 
// bytecode to any platform without that's why it is platorm independent.

// Java employs automatic garbage collection, handled by the JVM, which automates 
// memory management and reduces the risk of memory leaks.


// However, it occupies 4 bytes of memory for both 32 and 64-bit architectures in Java.

// Java supports call by value only. There is no call by reference in java.
// Java does not support operator overloading.  
// Java avoids multiple inheritance of classes to reduce complexity and uses 
// interfaces, enabling classes to implement multiple interfaces without the 
// associated issues.

// Java has no virtual keyword. We can override all non-static methods 
// by default. In other words, non-static methods are virtual by default


// Java always uses a single inheritance tree because all classes 
// are the child of the Object class in Java. The Object class is 
// the root of the inheritance tree in java.


// Java is also an object-oriented language. However, everything (except fundamental types) 
// is an object in Java. It is a single root hierarchy as everything gets derived from 
// java.lang.Object.

// Valid Java main() method signature
// ----------------------------------------------------------
public static void main(String[] args)  
public static void main(String []args)  
public static void main(String args[])  
public static void main(String... args)  
static public void main(String[] args)  
public static final void main(String[] args)  
final public static void main(String[] args)  
final strictfp public static void main(String[] args) 

// Invalid Java main() method signature
// ----------------------------------------------------------
public void main(String[] args)  
static void main(String[] args)  
public void static main(String[] args)  
abstract public static void main(String[] args)  

// The main() method is the entry point of any Java program. 
// It is the method where the program execution begins.


// - Can you save a Java source file by another name than the class name?

// Yes, if the class is not public. 

// class Simple{
// public static void main(String[] args) {
// 	System.out.println("Hello");
// }
// }

// To compile:
// 	javac Hard.java
// To execute:
// 	java Simple

// Observe that, we have compiled the code with file name but running the program 
// with class name. Therefore, we can save a Java program other than class name.

memory in bits (1 byte=8 bits)
-----------------------------------------------
boolean - 1
byte - 8
short - 16
char - 16
int - 32
long - 64 , "suffix - L"
float - 32, "suffix - f"
double - 64, "suffix - d"

Java is an object-oriented language, and sometimes you need to treat primitive 
data types as objects. Wrapper classes allow you to do this.

byte: Byte
short: Short
int: Integer
long: Long
float: Float
double: Double
char: Character
boolean: Boolean

Reference data type
------------------------------------------------------
Class,Interface,Array,String
Number - parent class of Integer,Double

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your age: ");
        if (scanner.hasNextInt()) {
            int age = scanner.nextInt();
            System.out.println("Age: " + age);
        } else {
            System.out.println("Invalid input. Please enter an integer.");
            return; // Exit the program or handle the error appropriately
        }

        System.out.print("Enter your salary: ");
        double salary = scanner.nextDouble();

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Salary: " + salary);

        scanner.close(); // Close Scanner

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Enter your name again with buffer reader: ");
            String name2 = reader.readLine();

            System.out.println("Name from reader: " + name2);
            reader.close(); // close reader.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}