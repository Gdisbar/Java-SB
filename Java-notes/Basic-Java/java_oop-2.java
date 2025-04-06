✅ Inheritance
-------------------------------------------------------------------------
Inheritance is the process by which a child class (subclass) acquires the properties and 
behaviors (fields and methods) of a parent class (superclass).
In Java, inheritance is implemented using the extends keyword

Inheritance enables:
Code reusability – The child class can reuse existing functionality of the parent class.
Extensibility – The child class can add or modify behavior.
Polymorphism – Enables dynamic method binding.


Access Modifiers and Inheritance
--------------------------------------------------------------------------------
- Methods declared public in a superclass also must be public in all subclasses.
- Methods declared protected in a superclass must either be protected or public in 
subclasses; they cannot be private.
- Methods declared private are not inherited at all, so there is no rule for them.


Single Inheritance (One class extends one superclass.)
--------------------------------------------------------------------------------
public class Vehicle {
    protected String make;
    protected String model;
    
    // Vehicle methods
}

public class Car extends Vehicle {
    private int numberOfDoors;
    private boolean isConvertible;
    
    // Car-specific methods
}

Multi-level Inheritance
--------------------------------------------------------------------------------
A chain of inheritance where a class extends another class, which in turn extends another class.

public class Vehicle {
    protected String make;
    protected String model;
    
    // Vehicle methods
}

class Car extends Vehicle {
    protected int numberOfDoors;
    
    // Car methods
}

class SportsCar extends Car {
    private int horsePower;
    private boolean hasTurbo;
    
    // SportsCar methods
}

Hierarchical Inheritance (Multiple classes extend from a single superclass)
------------------------------------------------------------------------------------
If you want to declare Developer or Manager class as public, private or protected  
do that in separate file save them with Developer.java & Manager.java - otherwise compiler 
will show error since 
In Java, a source file (.java file) can contain at most one public top-level class 
(a class not nested within another class). The name of this public class must match the 
name of the file (excluding the .java extension).

public class Employee {
    // Common employee attributes and methods
}

class Developer extends Employee {
    // Developer-specific attributes and methods
}

class Manager extends Employee {
    // Manager-specific attributes and methods
}

Multiple Inheritance (through interfaces )
-----------------------------------------------------------------------------
🎯 interface
---------------------------------------------------------------------
While a Java source file can have at most one public top-level class, this restriction 
doesn't apply to interfaces. You can have multiple public interfaces in a single .java file.

Only method signatures & constants, no implementation.
Achieves multiple inheritance & Abstraction.

In Java 8+, interfaces can have default and static methods:
-------------------------------------------------------------------------
public interface DatabaseHandler {
    // Static constant
    final int MAX_REQUEST_LEN = 20;
    void connect();
    // more interfaces
}

public interface Logger {
    void log(String message);
    
    // Default method
    default void logInfo(String message) {
        log("INFO: " + message);
    }
    
    default void logError(String message) {
        log("ERROR: " + message);
    }
    
    default void logError(String message, Exception e) {
        log("ERROR: " + message + " - " + e.getMessage());
    }
    
    // Static method
    static Logger getConsoleLogger() {
        return message -> System.out.println(message);
    }
}

// Simple implementation
public class DataProcessor implements DatabaseHandler, Logger {
    private String filePath;
    
    public DataProcessor(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public void connect() {
        // Implementation for connecting to database
    }
    
    @Override
    public void log(String message) {
        // Implementation to write to file
    }
    // DataProcessor-specific methods
}

🎯 Accessing classes from same/other packages
---------------------------------------------------------------------------
// A.java
package com.org.java_package;

public class A { // Public class, accessible from anywhere

    public int publicVar = 1;
    protected int protectedVar = 2;
    int defaultVar = 3; // Package-private
    private int privateVar = 4;

    public void publicMethod() {
        System.out.println("Public method in A");
        System.out.println("privateVar from A: " + privateVar); // Can access private members
    }

    protected void protectedMethod() {
        System.out.println("Protected method in A");
    }

    void defaultMethod() {
        System.out.println("Default method in A");
    }

    private void privateMethod() {
        System.out.println("Private method in A");
    }

    public void accessPrivateFromPublic(){
        privateMethod();
    }
}

// B.java
package com.org.java_package;

public class B extends A { // Public class, accessible from anywhere

    public void accessFromB() {
        System.out.println("publicVar from B: " + publicVar);
        System.out.println("protectedVar from B: " + protectedVar);
        System.out.println("defaultVar from B: " + defaultVar);
        //System.out.println("privateVar from B: " + privateVar); // Error: privateVar not accessible

        publicMethod();
        protectedMethod();
        defaultMethod();
        //privateMethod(); // Error: privateMethod not accessible
    }

    @Override
    protected void protectedMethod() { // Overriding protected method
        System.out.println("Overridden protected method in B");
        super.protectedMethod(); // Calling the superclass version
    }

    void accessProtectedFromSamePackage(){
        protectedMethod();
    }

    void accessDefaultFromSamePackage(){
        defaultMethod();
    }
}

// Main.java (in a different package, e.g., com.org.another_package)
package com.org.another_package;

import com.org.java_package.A;
import com.org.java_package.B;

public class Main {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();

        System.out.println("Accessing from Main (different package):");
        System.out.println("a.publicVar: " + a.publicVar);
        a.publicMethod();

        System.out.println("b.publicVar: " + b.publicVar);
        b.publicMethod();
        b.accessFromB();

        //System.out.println("a.protectedVar: " + a.protectedVar); // Error: protectedVar not accessible
        //a.protectedMethod(); // Error: protectedMethod not accessible

        //System.out.println("a.defaultVar: " + a.defaultVar); // Error: defaultVar not accessible
        //a.defaultMethod(); // Error: defaultMethod not accessible

        //a.privateVar; // Error: private var not accessible
        //a.privateMethod(); // error: private method not accessible

    }
}

// OtherClass.java(in same package)
package com.org.java_package;
class OtherClass{
    void accessA(){
        A a = new A();
        System.out.println(a.publicVar);
        System.out.println(a.protectedVar);
        System.out.println(a.defaultVar);
        a.publicMethod();
        a.protectedMethod();
        a.defaultMethod();
        a.accessPrivateFromPublic();
    }
}




🎯 super Keyword
---------------------------------------------------------------------
super refers to the parent class object.

Use case 
-----------------------------------------------------------------------------------
Call the superclass's constructor
Access the superclass's methods and fields
Explicitly use the superclass's implementation when overriding methods

class Parent {
    void display() {
        System.out.println("Parent class method");
    }
}

class Child extends Parent {
    void display() {
        super.display(); // Calls parent class method - 1st
        System.out.println("Child class method"); // 2nd
    }
}

Child obj = new Child();
obj.display();

// Parent class method  
// Child class method  


🎯 Constructor Chaining in Inheritance
-------------------------------------------------------------------------------------
Constructor chaining is the process of calling one constructor from another within the 
same class or from a subclass. In inheritance, the constructor of the superclass is 
automatically called before the subclass's constructor.

class Parent {
    Parent() {
        System.out.println("Parent Constructor");
    }
}

class Child extends Parent {
    Child() {
        super(); // Calls parent constructor - 1st 
        System.out.println("Child Constructor"); // 2nd
    }
}


✅ Polymorphism
----------------------------------------------------------------------------------
🎯 Compile-Time Polymorphism (Method Overloading)
------------------------------------------------------------------------------
Multiple methods with the same name but different parameters or data types.

class MathUtil {
    int sum(int a, int b) {
        return a + b;
    }

    int sum(int a, int b, int c) {
        return a + b + c;
    }
}

🎯 Runtime Polymorphism (Method Overriding)
------------------------------------------------------------------------------------
- Runtime polymorphism occurs when a subclass provides a specific implementation of a method 
that is already defined in its superclass. 
- This is resolved at runtime based on the actual object type.


🎯 instanceof Operator
-----------------------------------------------------------------------------------------
The instanceof operator checks if an object is an instance of a specific class or interface.
However, in modern Java applications, polymorphism is generally preferred over instanceof checks when possible, as it leads to more maintainable and extensible code

In Java 16+, you can use pattern matching with instanceof:
---------------------------------------------------------------------------------------
interface Shape {
    double calculateArea();
}

class Circle implements Shape {
    private double radius;
    //setter
    public Circle(double radius) {}
    //getter
    public double getRadius() {}

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

public void processShape(Shape shape) {
    System.out.println("Area: " + shape.calculateArea());
    
    // Pattern matching with instanceof (Java 16+)
    if (shape instanceof Circle circle) {
        System.out.println("Circle radius: " + circle.getRadius());
    } else if (shape instanceof Rectangle rectangle) {
        System.out.println("Rectangle dimensions: " + rectangle.getWidth() + " x " + rectangle.getHeight());
    }
}

ShapeProcessor processor = new ShapeProcessor();
processor.processShape(new Circle(5.0));


✅ Abstraction
-----------------------------------------------------------------------------------
Abstraction means hiding implementation details and showing only essential features. 
In Java, abstraction is achieved through abstract classes and interfaces.

Abstract Class -> Abstract + Concrete,	
Interface -> Only Abstract // check inheritance

🎯 Abstract Class
-----------------------------------------------------------------------------
Cannot be instantiated.
Can have abstract methods and concrete methods.
Abstract methods that have no implementation must be overridden by concrete subclasses.

// Abstract class representing an animal
abstract class Animal {
    // Abstract method that must be implemented by concrete subclasses
    abstract void makeSound();

    // Concrete method that can be inherited
    public void eat() {
        System.out.println("Animal is eating.");
    }
}

// Concrete subclass representing a Dog
class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("Woof!");
    }

    public void fetch() {
        System.out.println("Dog is fetching.");
    }
}

// Concrete subclass representing a Cat
class Cat extends Animal {
    @Override
    void makeSound() {
        System.out.println("Meow!");
    }

    public void purr() {
        System.out.println("Cat is purring.");
    }
}

// You cannot create an instance of an abstract class directly:
// Animal genericAnimal = new Animal(); // This will cause a compile-time error

// You can create instances of concrete subclasses & call fetch(),purr()
Dog myDog = new Dog();
Cat myCat = new Cat();

// You can use polymorphism to treat subclasses as instances of the abstract class:
// but can't call subclass-specific methods fetch(),purr() using abstract class reference
//  can only call abstract class specific methods - makeSound(),eat()
Animal animal1 = new Dog();
Animal animal2 = new Cat();

