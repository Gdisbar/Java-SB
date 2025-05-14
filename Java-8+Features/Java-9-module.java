One of the **core advantages of JPMS (Java Platform Module System)** is that it allows you
 to **encapsulate parts of a package** that would otherwise be fully accessible if the
  package is public — **even within the same package** across modules.

---

## 🔐 Problem with Traditional Java:
Before Java 9, **everything in a public package was accessible to everyone** who 
imported it — you couldn't restrict access at the module level.

---

## ✅ JPMS for Extra Encapsulation (Same Package, Different Modules)

Let’s say you have a package `com.bank.account` with two classes:
- `InternalLogger` — should only be used by `com.bank.account`
- `AccountManager` — intended to be public/exported

### 🏗️ Goal:
- Restrict `InternalLogger` so only internal module code can use it.
- Export only `AccountManager` to other modules.

---

## 🔧 Step-by-Step Setup

### 📂 Directory Structure:
```
banking/
└── src/
    ├── com.bank.account/
    │   ├── module-info.java
    │   ├── AccountManager.java
    │   └── InternalLogger.java
    └── com.external.audit/
        ├── module-info.java
        └── AuditTool.java
```

---

### 📄 `AccountManager.java`
```java
package com.bank.account;

public class AccountManager {
    public void openAccount() {
        InternalLogger.log("Opening account");
        System.out.println("Account opened");
    }
}
```

---

### 📄 `InternalLogger.java`
```java
package com.bank.account;

class InternalLogger {
    static void log(String msg) {
        System.out.println("LOG: " + msg);
    }
}
```

> `InternalLogger` has **package-private** access. But you want to be sure
 **external modules can’t access it**, even via reflection.

---

### 📄 `module-info.java` (for `com.bank.account`)
```java
module com.bank.account {
    exports com.bank.account; // Exports only public types
}
```

This ensures:
- `AccountManager` is accessible outside.
- `InternalLogger` is **not exported** and cannot be used/reflected externally.

---

### 📄 `AuditTool.java` (in another module)
```java
package com.external.audit;

import com.bank.account.InternalLogger; // ❌ This will fail

public class AuditTool {
    public static void main(String[] args) {
        InternalLogger.log("Trying to access logger");
    }
}
```

### 📄 `module-info.java` (for `com.external.audit`)
```java
module com.external.audit {
    requires com.bank.account;
}
```

---

## ✅ Compile and Run

```bash
javac -d out --module-source-path src $(find src -name "*.java")

java --module-path out -m com.external.audit/com.external.audit.AuditTool
```

### 🔴 You’ll Get a Compile-Time Error:
```
com.external.audit.AuditTool.java:3: error: InternalLogger is not public in com.bank.account; 
cannot be accessed from outside package
```

---

## 🔐 Why This Is Powerful

Even if:
- `InternalLogger` is in the same package as `AccountManager`
- The consuming module does a `requires com.bank.account`

…it **cannot access** `InternalLogger` unless:
- You explicitly `exports com.bank.account` AND
- `InternalLogger` is `public`.

This **module-level boundary** adds a strong layer of encapsulation — **much stronger than just package-private visibility**.

---

### 🔐 **Strongly Encapsulated JDK Internals** (Java 9+)

**Definition:**  
Starting from **Java 9**, the Java Platform Module System (JPMS) **strongly encapsulates** internal JDK APIs, meaning **internal packages like `sun.misc.*` are no longer accessible by default**.

---

### ✅ **Why was this done?**

1. **Security**: Prevent misuse of undocumented, unstable internal APIs.
2. **Stability**: Reduce risk from apps breaking due to changes in internal APIs.
3. **Encapsulation**: Enforce modular, well-defined boundaries between APIs.

---

### 🔧 Before Java 9

You could freely use internal classes like:
```java
sun.misc.Unsafe
com.sun.crypto.provider.SunJCE
```

These were never officially supported but widely used in frameworks, libraries, etc.

---

### 🚫 After Java 9 (With JPMS)

You **can't access** such classes unless the module `java.base` explicitly exports them:
```bash
# Example error:
"package sun.misc is not accessible"
```

---

### 🧪 How to **(temporarily)** bypass this restriction (not recommended for production):

Use JVM options:
```bash
--add-exports java.base/sun.misc=ALL-UNNAMED
--add-opens java.base/java.lang=ALL-UNNAMED
```

Example:
```bash
java --add-exports java.base/sun.misc=ALL-UNNAMED -jar myapp.jar
```

But this is a **band-aid**—the real solution is to stop using internal APIs.

---

### ✅ Real Example

Frameworks like Spring or Hibernate used to access:
```java
sun.reflect.ReflectionFactory
```

After JDK 9, they had to:
- Refactor to use **standard APIs**,
- Or ship with the necessary `--add-exports` flags.

---

### 🔍 How this is achieved in JPMS

- The JDK itself is split into modules (like `java.base`, `java.sql`).
- Each module **exports** only specific packages.
- Internals stay **hidden unless exported** with `exports` or `opens`.

---

### 📦 Example of `module-info.java`

```java
module my.module {
    requires java.base;
    // No access to sun.misc, sun.security.*, etc.
}
```

To access internals (bad practice):
```java
--add-exports java.base/sun.security.util=your.module.name
```

---

### ✅ Summary

| Feature                              | Status |
|--------------------------------------|--------|
| Access to `sun.misc`, `com.sun.*`    | 🔒 Blocked by default (Java 9+) |
| JVM option workaround                | ✅ Temporary but risky |
| Long-term solution                   | ✅ Use public APIs or jdk.internal.* only if explicitly exported |
| Purpose                              | ✅ Improve modularity, security, and maintainability |

---

Would you like a list of most commonly used blocked internal APIs and their official alternatives?

Certainly! Here's a list of the **most commonly used `JShell` commands** that make experimenting with Java quick and interactive:

---

## ✅ **Top JShell Commands**

> All special commands in JShell begin with `/` and are **not** Java code.

| Command | Description |
|--------|-------------|
| `/exit` | Exit the JShell session |
| `/help` | List all available JShell commands |
| `/vars` | Show all declared variables |
| `/methods` | Show all declared methods |
| `/list` | List all snippets (variables, methods, expressions, etc.) |
| `/types` | Show declared types (classes, interfaces, enums) |
| `/save <filename>` | Save your current session to a file |
| `/open <filename>` | Load and run code from a file |
| `/edit` | Opens an editor to modify snippets |
| `/reset` | Reset the JShell workspace (clears all snippets) |
| `/drop <id>` | Drop a specific snippet (variable, method, etc.) |
| `/reload` | Reload the last saved state |
| `/imports` | View current imports in the session |

---

## 🧪 **Example Usage**
```jshell
jshell> int x = 5;
x ==> 5

jshell> int y = 10;
y ==> 10

jshell> x + y
$3 ==> 15

jshell> /vars
|  int x = 5
|  int y = 10
|  int $3 = 15

jshell> /methods
// Shows custom methods you define

jshell> /save mysession.jsh
// Saves your session to a file
```

---

## 🛠️ **Handy Tips**
- You can define classes and methods:
```jshell
jshell> class Person {
   ...> String name;
   ...> Person(String name) { this.name = name; }
   ...> String greet() { return "Hi " + name; }
   ...> }

jshell> new Person("Alex").greet()
$1 ==> "Hi Alex"
```

- View documentation:
```jshell
jshell> /?   // Shortcut for help
```

---
Absolutely! Here's a well-structured, **Java 9+ feature showcase** with **code examples** covering:

- ✅ New String methods (Java 11)
- ✅ Switch expressions (Java 14+)
- ✅ Records (Java 14+)
- ✅ Files.readString/writeString (Java 11)
- ✅ Stream API enhancements (Java 9–16)
- ✅ Pattern Matching with `instanceof` (Java 16+)

---

## ✅ 1. **New String Methods** (Java 11)

```java
public class StringFeatures {
    public static void main(String[] args) {
        String text = "  Hello Java  ";
        
        System.out.println(text.isBlank());         // false
        System.out.println("   ".isBlank());        // true

        System.out.println("Java\nPython\nGo".lines().count());  // 3

        System.out.println(text.strip());           // "Hello Java"
        System.out.println(text.stripLeading());    // "Hello Java  "
        System.out.println(text.stripTrailing());   // "  Hello Java"

        System.out.println("Java".repeat(3));       // JavaJavaJava
    }
}
```

---

## ✅ 2. **Switch Expressions** (Java 14 - Finalized)

```java
public class SwitchExpressionDemo {
    public static void main(String[] args) {
        String day = "TUESDAY";

        String day = switch (day) {
            case "SATURDAY", "SUNDAY" -> "Weekend";
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };

        System.out.println(type);  // Weekday

        int day = 0;

        String dayOfWeek = switch(day){
        case 0 -> {
            // some other logic here
            yield "Weekend : SATURDAY";
        }
        case 1 -> "Weekend : SUNDAY";
        case 2,3,4,5,6 -> "Weekday";
        default -> throw new IllegalArgumentException("Invalid day: " + day);
        };

        System.out.println(dayOfWeek);
    }
}
```

---

## ✅ 3. **Records** (Java 14 - Preview, Java 16 - Finalized)

```java
public record Person(String name, int age) { }

public record Person(String name, int age) { // compact constructor - either above or this one
    Person{
        if(name==null)
            throw new IllegalArgumentException("name can't be null");
    }
}

class RecordTest {
    public static void main(String[] args) {
        Person p = new Person("Alice", 30);
        System.out.println(p.name());  // Alice
        System.out.println(p);         // Person[name=Alice, age=30]
    }
}
```

---

## ✅ 4. **File API: `readString`, `writeString`** (Java 11)

```java
import java.io.IOException;
import java.nio.file.*;

public class FileStringDemo {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("demo.txt");

        Files.writeString(path, "Java 11 is cool!");

        String content = Files.readString(path);
        System.out.println(content);  // Java 11 is cool!
    }
}
```

---

## ✅ 5. **Stream API Enhancements**

### 🔸 `takeWhile`, `dropWhile` (Java 9)

```java
import java.util.List;

public class StreamEnhancements {
    public static void main(String[] args) {
        List<Integer> nums = List.of(1, 2, 3, 6, 7, 1, 2);

        nums.stream().takeWhile(n -> n < 5).forEach(System.out::print);  // 123
        System.out.println();
        nums.stream().dropWhile(n -> n < 5).forEach(System.out::print);  // 6712
    }
}
```

### 🔸 `ofNullable` (Java 9)

```java
import java.util.stream.Stream;

public class NullableStream {
    public static void main(String[] args) {
        Stream<String> s = Stream.ofNullable(null); // returns empty stream, avoids NPE
        System.out.println(s.count());              // 0
    }
}
```

---

## ✅ 6. **Pattern Matching for `instanceof`** (Java 16)

```java
public class PatternMatching {
    public static void main(String[] args) {
        Object obj = "Hello pattern";

        if (obj instanceof String s) {
            System.out.println(s.toUpperCase());  // HELLO PATTERN
        }
    }
}
```

> No need to explicitly cast `obj` to `String` — it's matched and cast in one step.

---

## 🎁 Summary Table

| Feature                            | Version | Example Ref                  |
|-----------------------------------|---------|------------------------------|
| New `String` Methods              | 11      | `.strip()`, `.lines()`, etc. |
| Switch Expressions                | 14+     | `switch (...) ->`            |
| Records                           | 14+     | `record Person(...)`         |
| Files.readString/writeString      | 11      | `Files.readString(Path)`     |
| Stream Enhancements               | 9–16    | `takeWhile`, `dropWhile`     |
| Pattern Matching for `instanceof` | 16+     | `if (obj instanceof Type x)` |

---

Would you like this compiled into a ready-to-run Java file or an interactive JShell session script?