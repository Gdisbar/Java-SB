// Is-A (Inheritance)
class Animal {
    void eat() {
        System.out.println("Animal is eating.");
    }
}

class Dog extends Animal { // Dog "is-a" Animal
    void bark() {
        System.out.println("Dog is barking.");
    }
}

// Has-A (Composition)
class Engine {
    void start() {
        System.out.println("Engine started.");
    }
}

class Car {
    private Engine engine; // Car "has-a" Engine

    public Car() {
        this.engine = new Engine(); // Composition: Engine is created within Car
    }

    void drive() {
        engine.start();
        System.out.println("Car is driving.");
    }
}

// Aggregation (A weaker form of Has-A)
class Wheel {
    void rotate() {
        System.out.println("Wheel rotating");
    }
}

class Vehicle {
    private Wheel[] wheels;

    public Vehicle(Wheel[] wheels) { // Wheel can exist without Vehicle
        this.wheels = wheels;
    }

    void move(){
        for(Wheel w : wheels){
            w.rotate();
        }
        System.out.println("Vehicle moving");
    }
}

public class RelationshipExample {
    public static void main(String[] args) {
        // Is-A Example
        Dog myDog = new Dog();
        myDog.eat(); // Inherited from Animal
        myDog.bark();

        // Has-A (Composition) Example
        Car myCar = new Car();
        myCar.drive();

        // Aggregation Example
        Wheel wheel1 = new Wheel();
        Wheel wheel2 = new Wheel();
        Wheel wheel3 = new Wheel();
        Wheel wheel4 = new Wheel();
        Wheel[] vehicleWheels = {wheel1, wheel2, wheel3, wheel4};
        Vehicle myVehicle = new Vehicle(vehicleWheels);
        myVehicle.move();

    }
}

✅ Tight Coupling -  When one class directly creates an instance of another specific class, 
rather than relying on an interface.

✅ Loose Coupling -  Using interfaces or abstract classes to define contracts, allowing 
different implementations to be swapped in and out.


✅ Static and Final Keywords
----------------------------------------------------------------------------------------
🔥 1. Static Keyword
The static keyword in Java is used to define class-level members (variables, methods, blocks, and nested classes).

When a member is marked as static, it belongs to the class rather than an instance of the class.
Static members are shared across all instances of the class.
static members are loaded into memory when the class is first loaded (before any objects are created).


🎯 1.1 Static Variables
-------------------------------------------------------------------------------
Memory is allocated once when the class is loaded into memory.

Use case 
-----------------------------------------------------------------
Configuration settings that apply to all instances
Shared resource pools (database connections, thread pools)
Application constants
Counters or statistics tracking

class Counter {
    static int count = 0;

    Counter() {
        count++;
        System.out.println("Count: " + count);
    }
}

Counter c1 = new Counter(); // Count = 1
Counter c2 = new Counter(); // Count = 2
Counter c3 = new Counter(); // Count = 3


🎯 1.2 Static Methods
----------------------------------------------------------------
Declared using the static keyword.
Can only access static variables and call static methods directly.
Cannot access non-static variables or methods directly.
Can be called without creating an object.

Use case 
-----------------------------------------------------------------
Utility classes (StringUtils, CollectionUtils, FileUtils)
Factory methods
Helper functions that don't require instance state
Mathematical calculations
Parsers and formatters

class Utility {
    static void show() {
        System.out.println("This is a static method.");
    }
}

Utility.show(); // Directly call without object


🎯 1.3 Static Block
------------------------------------------------------------------------------
A static block is executed once when the class is loaded into memory.
Execution order : static block(1 time class loading) > default constructor(object creation)
"main" method : after class loading (i.e static block) -> program start execution
Used to initialize static data or perform one-time setup tasks.

Use case 
-----------------------------------------------------------------
Loading configuration files
Setting up shared resources
Initializing logging
One-time setup operations that must occur before any instances are created

class Demo {
    static {
        System.out.println("Static block executed");
    }

    Demo() {
        System.out.println("Constructor executed");
    }
}

Demo obj1 = new Demo();
Demo obj2 = new Demo(); // Static block won't execute again

// Static block executed  
// Constructor executed  
// Constructor executed  

🔥 2. Final Keyword
------------------------------------------------------------------------------------
🎯 2.1 Final Variables
------------------------------------------------------------------------------
Declared using final keyword.
Value must be assigned at the time of declaration or within the constructor.
Once assigned, the value cannot be changed.

Use case 
-----------------------------------------------------------------
Constants and configuration values
Defensive programming (preventing accidental reassignment)
Thread safety (ensuring values don't change after initialization)
Implementation of immutable objects

class Demo {
    final int MAX_VALUE = 100;

    void display() {
        // MAX_VALUE = 200; // Error: cannot assign value to final variable
        System.out.println(MAX_VALUE);
    }
}


🎯 2.2 Final Methods
--------------------------------------------------------------------------------
A final method cannot be overridden by a subclass(i.e  child classes).
It ensures that the method's behavior remains unchanged.

Use case 
-----------------------------------------------------------------
Security-critical methods
Algorithms that must be implemented in a specific way
Template methods where only certain steps should be customizable
Methods that enforce invariants


class Parent {
    final void show() {
        System.out.println("Parent class method");
    }
}

class Child extends Parent {
    // void show() {}  // ERROR: Cannot override final method
}

Child obj = new Child();
obj.show();

🎯 2.3 Final Classes
---------------------------------------------------------------------
A final class cannot be inherited(i.e extended).
Used to prevent modification of critical class behavior.

Use case 
-----------------------------------------------------------------
Security-critical classes
Immutable classes (like Java's String, Integer, etc.)
Utility classes that shouldn't be extended
Classes that have a complete implementation and shouldn't be modified

final class Parent {
    void display() {
        System.out.println("Parent class");
    }
}

// class Child extends Parent {}  // ERROR: Cannot inherit from final class



✅ Inner Classes and Nested Classes
----------------------------------------------------------------------------------
"Nested class" is a general term that encompasses both static and non-static inner classes.
Therefore, both static inner classes and non-static inner classes are nested classes.

🎯 1.Static Inner Class (Static Nested Class) 
--------------------------------------------------------------------------------
A static inner class is declared using the static keyword within another class.

class Outer {
    static int outerStaticVar = 10;
    int outerNonStaticVar = 20;

    static class StaticInner {
        void innerMethod() {
            System.out.println("Outer static variable: " + outerStaticVar);
            //System.out.println("Outer non-static variable: " + outerNonStaticVar); //error, cannot access non-static
        }
    }
}

Outer.StaticInner inner = new Outer.StaticInner();
inner.innerMethod();

🎯 2. Non-Static Inner Class (Inner Class):
--------------------------------------------------------------------------------
A non-static inner class is declared without the static keyword within another class.


class Outer {
    static int outerStaticVar = 10;
    int outerNonStaticVar = 20;

    class NonStaticInner {
        void innerMethod() {
            System.out.println("Outer static variable: " + outerStaticVar);
            System.out.println("Outer non-static variable: " + outerNonStaticVar);
        }
    }
}

Outer outer = new Outer();
Outer.NonStaticInner inner = outer.new NonStaticInner();
inner.innerMethod();

🔥 Use Cases:
-------------------------------------------------------------------------------
Static Inner Class:
----------------------------------------
- Useful for grouping related utility classes or helper classes within an outer class.
- When you want to logically associate a class with an outer class but don't need access to 
the outer class's instance members.

Non-Static Inner Class:
---------------------------------------------
- Useful when the inner class needs to access the outer class's instance members.
- When you want to tightly couple the inner class's functionality to the outer class's state.
- Event handlers are often implemented as non static inner classes.


🔥 Feature              |     Static Inner Class       |    Non-Static Inner Class   
-----------------------------------------------------------------------------------------------           
Instance Dependency     -> Independent of outer class -> instance Requires outer class instance               
Access to Outer Members ->  Only static members       ->Static and non-static members               
Instantiation           -> Outer.StaticInner inner    ->Outer outer = new Outer();
                           = new Outer.StaticInner();   Outer.NonStaticInner inner 
                                                        = outer.new NonStaticInner();             
Implicit reference      ->  No                        ->Yes  
to outer class instance           
Memory usage            -> Uses less memory, as it    -> Uses more memory, as it holds a 
                        does not hold a reference to     reference to the outer class instance.
                        the outer class instance.                
                        
                        