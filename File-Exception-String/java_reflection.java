# **Reflection and Annotations in Java**  
Java provides powerful mechanisms for **introspection** and **metadata handling** 
through **Reflection** and **Annotations**. These features enable runtime access to 
the structure of classes, methods, fields, and metadata, allowing Java programs to 
inspect and modify behavior dynamically.

---

# 🟠 **1. Reflection in Java**
Reflection in Java allows a program to **inspect and modify the structure and behavior** 
of classes, methods, fields, and constructors at **runtime**.  

---

## ✅ **1.1 What is Reflection?**  
- Reflection is part of the `java.lang.reflect` package.  
- It allows:  
   ✅ Inspecting classes, methods, fields, and constructors.  
   ✅ Accessing private members.  
   ✅ Invoking methods dynamically at runtime.  
   ✅ Creating objects at runtime.  
   ✅ Modifying method behavior at runtime.  

---

## ✅ **1.2 Why Reflection is Useful**  
✔️ Used in frameworks like **Spring**, **Hibernate**, and **JUnit**.  
✔️ Essential for building **dynamic applications** and **dependency injection**.  
✔️ Used in **ORM frameworks** to map objects to database tables.  

---

## ✅ **1.3 How to Use Reflection**
Reflection is handled through the following key classes:  
| Class/Interface | Purpose |
|----------------|---------|
| `Class<?>` | Represents the class object |
| `Method` | Represents methods |
| `Field` | Represents fields |
| `Constructor` | Represents constructors |
| `Modifier` | Used to inspect method/field modifiers (e.g., `public`, `static`) |

---

## ✅ **1.4 Getting a `Class` Object**  
You can get the `Class` object in three ways:  
1. **Using `.class` property**  
2. **Using `Class.forName()`**  
3. **Using `getClass()`**  

---

### 🔥 **Getting Class Object**

public class ReflectionExample {
    public static void main(String[] args) throws Exception {
        // 1. Using .class
        Class<?> c1 = String.class;
        
        // 2. Using forName()
        Class<?> c2 = Class.forName("java.lang.String");
        
        // 3. Using getClass()
        String s = "Hello";
        Class<?> c3 = s.getClass();

        System.out.println("Class name: " + c1.getName()); //java.lang.String
        System.out.println("Class name: " + c2.getName()); // java.lang.String
        System.out.println("Class name: " + c3.getName()); // java.lang.String
    }
}




## ✅ **1.5 Inspecting Class Details**  
You can inspect the following class details:  
✔️ Class Name  
✔️ Fields  
✔️ Methods  
✔️ Constructors  
✔️ Modifiers  

---

### 🔥 Inspecting Class Details

import java.lang.reflect.*;

public class ReflectionExample {
    public static void main(String[] args) {
        Class<?> c = String.class;

        System.out.println("Class Name: " + c.getName());

        // Get Methods
        System.out.println("\nMethods:");
        for (Method method : c.getDeclaredMethods()) {
            System.out.println(method.getName());
        }

        // Get Fields
        System.out.println("\nFields:");
        for (Field field : c.getDeclaredFields()) {
            System.out.println(field.getName());
        }

        // Get Constructors
        System.out.println("\nConstructors:");
        for (Constructor<?> constructor : c.getDeclaredConstructors()) {
            System.out.println(constructor.getName());
        }
    }
}


---

### ✅ **Output**:
```
Class Name: java.lang.String

Methods:
charAt
indexOf
substring
...

Fields:
value
hash
...

Constructors:
java.lang.String
java.lang.String
...
```

---

## ✅ **1.6 Invoking Methods Using Reflection**  
You can call methods dynamically using `Method.invoke()`.

---

### 🔥 **Example: Invoke Method using Reflection**

import java.lang.reflect.Method;

public class InvokeExample {
    public static void main(String[] args) throws Exception {
        String str = "Hello World";

        // Get the method
        Method method = String.class.getMethod("toUpperCase");

        // Invoke the method
        String result = (String) method.invoke(str);

        System.out.println("Result: " + result); //HELLO WORLD
    }
}



---

✅ **1.7 Accessing Private Fields Using Reflection**
You can access private fields using `setAccessible(true)`.



### 🔥 **Example: Access Private Fields**

import java.lang.reflect.Field;

class Example {
    private String message = "Secret";
}

public class AccessPrivateField {
    public static void main(String[] args) throws Exception {
        Example obj = new Example();

        Field field = Example.class.getDeclaredField("message");
        field.setAccessible(true);

        System.out.println("Private Value: " + field.get(obj)); // Secret
    }
}


## ✅ **1.8 Creating Objects Using Reflection**
You can create objects dynamically using `newInstance()`.

---

### 🔥 **Example: Create Object Using Reflection**

class Example {
    public void display() {
        System.out.println("Reflection in Java");
    }
}

public class CreateObject {
    public static void main(String[] args) throws Exception {
        Example obj = Example.class.getDeclaredConstructor().newInstance();
        obj.display();
    }
}




---

## ✅ **1.9 Best Practices for Reflection**
✅ Use reflection sparingly — it’s slower than direct method calls.  
✅ Avoid using reflection in performance-critical code.  
✅ Ensure security permissions when accessing private fields.  

---

# 🟠 **2. Annotations in Java**
Annotations provide metadata that can be attached to classes, methods, fields, 
parameters, and packages.  

---

## ✅ **2.1 What are Annotations?**
- Introduced in **Java 5**.  
- Annotations provide **information to the compiler** or **frameworks**.  
- Used to implement **custom behavior** at compile-time or runtime.  

---

## ✅ **2.2 Built-in Annotations**  
| Annotation | Purpose |
|-----------|---------|
| `@Override` | Ensures a method overrides a superclass method |
| `@Deprecated` | Marks a method as deprecated |
| `@SuppressWarnings` | Suppresses compiler warnings |
| `@FunctionalInterface` | Marks an interface as functional |
| `@SafeVarargs` | Prevents unsafe operations on varargs |

---

### 🔥 **Example: @Override**

class Parent {
    void show() {
        System.out.println("Parent class");
    }
}

class Child extends Parent {
    @Override
    void show() {
        System.out.println("Child class");
    }
}



---

## ✅ **2.3 Custom Annotations**
You can create custom annotations using `@interface`.

---

### 🔥 **Example: Custom Annotation**

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface MyAnnotation {
    String value();
}

public class CustomAnnotation {
    @MyAnnotation(value = "Testing Custom Annotation")
    public void display() {
        System.out.println("Using custom annotation");
    }
}


---

## ✅ **2.4 Reading Custom Annotations using Reflection**

import java.lang.reflect.Method;

public class ReadAnnotation {
    public static void main(String[] args) throws Exception {
        Method method = CustomAnnotation.class.getMethod("display");
        MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
        System.out.println("Annotation value: " + annotation.value());
    }
}


---

### ✅ **Output**:
```
Annotation value: Testing Custom Annotation
```

---

## ✅ **Best Practices for Annotations**  
✅ Use annotations to reduce boilerplate code.  
✅ Define clear annotation contracts.  
✅ Use `RetentionPolicy.RUNTIME` if it’s needed at runtime.  

---

## ✅ ✅ ✅ **Summary**  
✔️ Reflection allows dynamic introspection and manipulation.  
✔️ Annotations simplify metadata handling.  
✔️ Avoid excessive use of reflection for performance-critical code.  

---

🔥 **Next Step:** Do you want to explore dependency injection or dynamic proxies using reflection? 😎