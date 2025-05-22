🚀 What is Object-Oriented Programming (OOP)?
----------------------------------------------------------------------------------------
<<<<<<< HEAD
Object-Oriented Programming (OOP) is a programming paradigm based on the concept of "objects" 
that represent real-world entities.
    
=======
Object-Oriented Programming (OOP) is a programming paradigm based on the 
concept of "objects" that represent real-world entities.

>>>>>>> 5cbda8c (Combined commit-6)
In OOP:

Objects = State (data) + Behavior (methods)
An Object is an instance of a Class.
OOP provides a way to structure a program using objects rather than 
actions and logic.

🏆 Core Principles of OOP
--------------------------------------------------------------------------------------
Encapsulation – Hiding internal details of an object and providing 
controlled access via methods.
Inheritance – A class (child) can inherit properties and behaviors from 
another class (parent).
Polymorphism – A single entity (method or operator) can behave differently 
depending on the context.
Abstraction – Hiding complex implementation details and exposing only the 
essential parts.

Data Handling - Data and functions are combined in objects
Code Reusability - 	Code is reusable through inheritance
Security - Controlled access through encapsulation

✅ Classes and Objects
-------------------------------------------------------------------------
A class is a blueprint or template for creating objects.
A class defines the state (stored in fields/attributes) and 
behavior (implemented through methods) of objects.

An object is an instance of a class.
Objects are created using the new keyword.

For example, a Car object might have:
--------------------------------------------------------------
States: color, model, year, currentSpeed
Behaviors: accelerate(), brake(), turnLeft(), turnRight()

🛠️ Constructors
-----------------------------------------------------------------------------
A constructor is a special method that:

Has the same name as the class.
Is used to initialize objects.
Is automatically called when an object is created.


Default Constructor – No parameters.
Parameterized Constructor – Takes arguments to initialize objects.
Copy Constructor – Creates a new object as a copy of an existing object.

Even if compile creates default constructor,we do it explicitly since 
we don't know how a object will be created in future (Parameterized or 
non-Parameterized)

Default value for reference type (Wrapper class :String,Integer) is null & 
for number it's 0

class Person {
    String name;
    int age;

    // Default Constructor
    Person() {
        name = "Unknown";
        age = 0;
    }

    // Parameterized Constructor
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Overloaded Constructor
    Person(String name,long age){
    	//code
    }

    // Copy Constructor
    Person(Person p) {
        this.name = p.name;
        this.age = p.age;
    }

    void display() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

public class Main {
    public static void main(String[] args) {
        Person p1 = new Person(); // Default constructor
        Person p2 = new Person("Alice", 25); // Parameterized constructor
        Person p3 = new Person(p2); // Copy constructor
        
        p1.display();
        p2.display();
        p3.display();
    }
}



🎯 this Keyword
-----------------------------------------------------------------
Differentiating instance variables from parameters:
------------------------------------------------------
public class Employee {
    private String name;
    
    public void setName(String name) {
        this.name = name; // 'this.name' refers to the instance variable 
    }
}

Calling another constructor: // Constructor chaining
-----------------------------------------------------------
public class DatabaseConnection {
    private String url;
    private String username;
    private String password;
    private int timeout;
    private boolean autoCommit;
    
    public DatabaseConnection(String url) {
        this(url, "admin", "password");
    }
    
    public DatabaseConnection(String url, String username, String password) {
        this(url, username, password, 30, true);
    }
    
    public DatabaseConnection(String url, String username, String password, 
                             int timeout, boolean autoCommit) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
        this.autoCommit = autoCommit;
    }
}

Returning the current object (method chaining):
-----------------------------------------------------------
public class QueryBuilder {
    private String table;
    private String[] columns;
    private String whereClause;
    
    public QueryBuilder select(String[] columns) {
        this.columns = columns;
        return this;
    }
    
    public QueryBuilder from(String table) {
        this.table = table;
        return this;
    }
    
    public QueryBuilder where(String condition) {
        this.whereClause = condition;
        return this;
    }
    
    public String build() {
        // Construct query string
        return "SELECT " + String.join(", ", columns) + 
               " FROM " + table + 
               (whereClause != null ? " WHERE " + whereClause : "");
    }
}

// Usage with method chaining
String query = new QueryBuilder()
    .select(new String[]{"id", "name", "salary"})
    .from("employees")
    .where("department_id = 10")
    .build();

Passing the current object as a parameter:
------------------------------------------------------------------
public class Employee {
    private String name;
    private Department department;
    
    public void assignToDepartment(Department dept) {
        this.department = dept;
        dept.addEmployee(this); // Passing current object to another method
    }
}

✅ Encapsulation
----------------------------------------------------------------------------
Encapsulation = Data Hiding + Controlled Access

The internal state of an object is hidden using the private modifier.
------------------------------------------------------------------------------

Validation: Ensuring data integrity before storing
Authentication: Verifying access rights
Logging: Recording operations for audit trails
Business Rules: Enforcing domain-specific rules

public class BankAccount {
    // Private data - hidden from outside
    private String accountNumber;
    private double balance;
    private String ownerName;
    private String pin;
    
    // Public interface - controlled access through getter
    public double getBalance() {
        // Perhaps add logging or authentication here
        return balance;
    }
    // Public interface - controlled access through setter
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            logTransaction("Deposit", amount);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }
    
    public boolean withdraw(double amount, String enteredPin) {
        if (!pin.equals(enteredPin)) {
            logSecurityEvent("Invalid PIN attempt");
            return false;
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        if (amount > balance) {
            return false;
        }
        
        balance -= amount;
        logTransaction("Withdrawal", amount);
        return true;
    }
    
    private void logTransaction(String type, double amount) {
        // Implementation of transaction logging
    }
    
    private void logSecurityEvent(String message) {
        // Implementation of security logging
    }
}



🔒 Access Modifiers
-----------------------------------------------------------------------
private: Accessible only within the declared class
default (no modifier): Accessible within the declared class & same package
protected: Accessible within the same package & by 
subclasses(even if they are in a different package)
public: Accessible from the same package + by subclasses + other packages

In Java, a source file (.java file) can contain at most one public top-level 
class (a class not nested within another class). The name of this public 
class must match the name of the file (excluding the .java extension).

While a Java source file can have at most one public top-level class, this 
restriction doesn't apply to interfaces. You can have multiple public 
interfaces in a single .java file.


proper use of access modifiers helps create
----------------------------------------------------------------------------
Robust APIs: Public methods form a stable interface
Implementation Hiding: Private details can change without affecting users
Package Architecture: Default visibility helps organize related classes
Extension Points: Protected members allow for controlled inheritance

The access modifier of a class's attributes (members) can be the same as or 
more restrictive (lower level) than the access modifier of the class itself.

class Engine {
    // Members of the Engine class can have various access modifiers.
    public void start() {
        System.out.println("Engine started.");
    }

    protected void warmUp() {
        System.out.println("Engine warming up.");
    }

    void idle() { // Default (package-private) access
        System.out.println("Engine idling.");
    }

    private void internalCheck() {
        System.out.println("Engine internal check.");
    }
}

public class Car {
    // The Car class is public, so its members can have public, protected, 
    // package-private, or private access.
    public String model;
    protected String color;
    String registrationNumber; // Default (package-private) access
    private Engine engine;

    public Car(String model, String color, String registrationNumber) {
        this.model = model;
        this.color = color;
        this.registrationNumber = registrationNumber;
        this.engine = new Engine(); // Initialize the engine
    }

    public void startCar() {
        engine.start(); // Public method of Engine can be accessed.
    }

    protected void prepareForDrive() {
        // Protected method of Engine can be accessed in this class and 
        // subclasses.
        engine.warmUp(); 
        System.out.println("Car prepared for drive.");
    }

    void showCarDetails() { // Default (package-private) access
        // Default method of Engine can be accessed in the same package.
        engine.idle(); 
        System.out.println("Model: " + model + ", Color: " + color + 
            ", Registration: " + registrationNumber);
    }

    private void performInternalCheck() {
        // Private method of Engine can be accessed only within 
        // the Engine class.Uncomment this line will cause compiler 
        // access-error
        // engine.internalCheck(); 
        System.out.println("Car internal check done.");
    }

    public static void main(String[] args) {
        Car myCar = new Car("Sedan", "Blue", "ABC-123");
        myCar.startCar();
        myCar.prepareForDrive();
        myCar.showCarDetails();
        //myCar.performInternalCheck(); // would cause a compiler error, as it is private.
    }
}

Since Car is public, its attributes can be public, protected, 
default (package-private), or private.
The Engine class is also accessible by the car class.
The Car class can call the public and protected methods of the engine class.
The Car class can call the default method of the engine class, 
if the Car class is in the same package as the Engine class.
The car class can not call the private method of the engine class.

