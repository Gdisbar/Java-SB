

Great question! `Optional` in Java 8 is a **container object** used to contain **not-null** values. It is a way to represent a **value that may or may not be present**, without using `null` directly.

---

## ✅ **Why Optional?**

Before Java 8:
```java
String name = getUserName();
if (name != null) {
    System.out.println(name.toUpperCase());
}
```

Java 8 onwards:
```java
Optional<String> name = getUserName();
name.ifPresent(n -> System.out.println(n.toUpperCase()));
```

It helps:
- Avoid `NullPointerException`
- Write cleaner, functional-style code
- Make the **"absence of value" explicit**, not implicit with `null`

---

## 🔧 **Creating an Optional**

```java
Optional<String> name = Optional.of("John");             // throws if null
Optional<String> empty = Optional.empty();               // empty optional
Optional<String> maybeName = Optional.ofNullable(null);  // safe wrapper
```

---

## 🔍 **Key Methods**

| Method                       | Description |
|-----------------------------|-------------|
| `isPresent()`               | Checks if value is present |
| `ifPresent(Consumer)`       | Runs lambda if value exists |
| `get()`                     | Gets value (throws if empty – avoid if possible) |
| `orElse(T)`                 | Returns value or default |
| `orElseGet(Supplier)`       | Lazy version of `orElse` |
| `orElseThrow(Supplier)`     | Throws exception if empty |
| `map(Function)`             | Transforms value if present |
| `flatMap(Function)`         | Flattens nested `Optional<Optional<T>>` |
| `filter(Predicate)`         | Returns empty if predicate fails |

---

## 🧪 **Examples**

### ✅ 1. Basic Usage

```java
Optional<String> name = Optional.of("Alice");
name.ifPresent(n -> System.out.println(n.length())); // prints 5
```

---

### ✅ 2. Default Value

```java
String value = Optional.ofNullable(null).orElse("default");
System.out.println(value);  // "default"
```

---

### ✅ 3. Optional in method return

```java
public Optional<User> findUserById(String id) {
    if (userExists(id)) {
        return Optional.of(userRepo.get(id));
    } else {
        return Optional.empty();
    }
}
```

---

### ✅ 4. Avoid `null`

Instead of:
```java
String name = user != null ? user.getName() : "Unknown";
```

Do:
```java
String name = Optional.ofNullable(user)
                      .map(User::getName)
                      .orElse("Unknown");
```

---

## 🧠 Tip

Avoid:
```java
if (optional.isPresent()) {
    return optional.get(); // smells like pre-Java 8
}
```

Do:
```java
return optional.orElse("default");
```

---

Want me to show a `List<Optional<T>>` filtering use case or `Optional` in stream chains?


Awesome! Let's walk through **`List<Optional<T>>` filtering** and **Optional in stream chains** — both are **very practical in real-world Java 8+ development**.

---

## ✅ Example 1: `List<Optional<T>>` Filtering Use Case

### 🎯 Scenario:
You have a list of `Optional<String>`, and you want to collect **only present values** into a `List<String>`.

### 💡 Code:
```java
import java.util.*;
import java.util.stream.Collectors;

public class OptionalListExample {
    public static void main(String[] args) {
        List<Optional<String>> optionalNames = Arrays.asList(
            Optional.of("Alice"),
            Optional.empty(),
            Optional.of("Bob"),
            Optional.empty(),
            Optional.of("Charlie")
        );

        List<String> names = optionalNames.stream()
            .filter(Optional::isPresent)         // keep only non-empty Optionals
            .map(Optional::get)                  // extract the value
            .collect(Collectors.toList());       // collect to list

        System.out.println(names); // Output: [Alice, Bob, Charlie]
    }
}
```

---

## ✅ Example 2: Cleaner Version Using `flatMap`

```java
List<String> names = optionalNames.stream()
    .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
    .collect(Collectors.toList());
```

### 💡 Why better?
- `flatMap` avoids `isPresent` + `get` pattern
- Keeps code cleaner, more functional

---

## ✅ Example 3: Optional in Stream Chains

### 🎯 Scenario:
Suppose you have a `List<User>`, and you want to get names of users **only if they have an email**.

```java
class User {
    String name;
    Optional<String> email;

    public User(String name, Optional<String> email) {
        this.name = name;
        this.email = email;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
```

### 💡 Code:
```java
List<User> users = Arrays.asList(
    new User("Alice", Optional.of("alice@mail.com")),
    new User("Bob", Optional.empty()),
    new User("Charlie", Optional.of("charlie@mail.com"))
);

List<String> userNamesWithEmail = users.stream()
    .filter(user -> user.getEmail().isPresent())
    .map(User::getName)
    .collect(Collectors.toList());

System.out.println(userNamesWithEmail); // Output: [Alice, Charlie]
```

---

### Or: Use `flatMap` to extract the emails themselves

```java
List<String> emails = users.stream()
    .map(User::getEmail)
    .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
    .collect(Collectors.toList());

System.out.println(emails); // Output: [alice@mail.com, charlie@mail.com]
```

---

## 🧠 Summary

| Use Case                            | Best Practice                |
|------------------------------------|------------------------------|
| `List<Optional<T>>` to `List<T>`   | Use `flatMap + orElseGet`   |
| Avoid `null` propagation            | Wrap with `Optional`        |
| Use in stream chains               | Combine `map`, `filter`, `flatMap` |
| Avoid `.get()` unless guaranteed   | Prefer `.orElse()`, `.map()` etc |

---

Want me to show a real-world use case with nested `Optional`s or chaining Optional inside DTO mappings?