




Great! Let’s dive into the **core stream operations** — `stream`, `filter`, `map`, and `collect` — and how to **combine them effectively**. These are **fundamental tools** in Java 8+ for writing clean, expressive, and functional-style code.

---

## 🧩 Breakdown of Concepts

### 1. `.stream()`
Turns a collection into a **stream pipeline** for processing.

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
Stream<String> nameStream = names.stream();
```

---

### 2. `.filter(Predicate)`
Used to **select elements** that match a condition.

```java
names.stream()
     .filter(name -> name.startsWith("A"))
```

---

### 3. `.map(Function)`
Used to **transform** elements from one form to another.

```java
names.stream()
     .map(String::toUpperCase)
```

---

### 4. `.collect(Collector)`
Used to **gather results** from a stream (into a list, map, set, etc.)

```java
names.stream()
     .collect(Collectors.toList());
```

---

## 🔁 Putting Them Together – Typical Pattern

```java
List<ResultType> result = list.stream()
    .filter(condition)         // step 1: select what you want
    .map(transformation)       // step 2: transform it
    .collect(Collector);       // step 3: gather the result
```

---

## 🧪 Examples

---

### ✅ Example 1: Filter Even Numbers and Square Them

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

List<Integer> squares = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .collect(Collectors.toList());

System.out.println(squares); // Output: [4, 16, 36]
```

---

### ✅ Example 2: Get Names of People Older Than 18

```java
class Person {
    String name;
    int age;
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() { return name; }
    public int getAge() { return age; }
}

List<Person> people = Arrays.asList(
    new Person("Alice", 20),
    new Person("Bob", 17),
    new Person("Charlie", 23)
);

List<String> adults = people.stream()
    .filter(p -> p.getAge() > 18)
    .map(Person::getName)
    .collect(Collectors.toList());

System.out.println(adults); // Output: [Alice, Charlie]
```

---

### ✅ Example 3: Convert List to Map with Filtering

```java
Map<String, Integer> nameToAge = people.stream()
    .filter(p -> p.getAge() > 18)
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge
    ));

System.out.println(nameToAge); // Output: {Alice=20, Charlie=23}
```

---

## 💡 How To Think When Combining

| Step | Operation     | Purpose                            | Example                                       |
|------|---------------|------------------------------------|-----------------------------------------------|
| 1    | `.stream()`   | Start the pipeline                 | `list.stream()`                               |
| 2    | `.filter()`   | Narrow down the data               | `.filter(x -> x > 10)`                        |
| 3    | `.map()`      | Transform each element             | `.map(x -> x * 2)`                            |
| 4    | `.collect()`  | Finalize result into list/map/set  | `.collect(Collectors.toList())`               |

---

## ✅ Pro Tips

- Use **`filter()` before `map()`** for performance if possible.
- Avoid using **`get()`** on Optionals inside `map` — use `flatMap` or `orElse`.
- Use **method references** where you can: `Person::getName` instead of `p -> p.getName()`.
- If returning primitives, consider `.mapToInt()` or `.mapToDouble()` for performance.

---

Want me to tailor examples for more complex use cases like `List<Map>`, nested filtering, flattening, or grouping?

---

## 🔁 Use Case 1: Flatten `List<Map<String, List<String>>>`

**Problem**: You have a `List<Map<String, List<String>>>` and want a `List<String>` of all values.

### ✅ Example:

```java
List<Map<String, List<String>>> data = Arrays.asList(
    Map.of("fruits", List.of("apple", "banana")),
    Map.of("vegetables", List.of("carrot", "potato"))
);

List<String> flatList = data.stream()
    .flatMap(map -> map.values().stream())     // Stream<List<String>>
    .flatMap(List::stream)                     // Flatten List<String>
    .collect(Collectors.toList());

System.out.println(flatList); // Output: [apple, banana, carrot, potato]
```

---

## 🔁 Use Case 2: Nested Filtering (Filter inside Map inside List)

Imagine a structure:  
```java
List<Customer> customers; // Customer has List<Order>
```

You want to get **List of orderIds** for **non-cancelled orders** from all customers.

### ✅ Example:

```java
List<String> orderIds = customers.stream()
    .flatMap(customer -> customer.getorderList().stream())        // Flatten all orders
    .filter(order -> !order.isCancelled())                        // Only non-cancelled
    .map(Order::getOrderID)
    .collect(Collectors.toList());
```

---

## 🔁 Use Case 3: Group Orders by Customer ID and Count Them

```java
Map<String, Long> orderCountPerCustomer = orders.stream()
    .collect(Collectors.groupingBy(
        Order::extractCustomer,
        Collectors.counting()
    ));
```

---

## 🔁 Use Case 4: Group by a Field and Collect Into Nested List

**Goal**: Group orders by `trackingDetails` and return `List<OrderID>` per status.

```java
Map<String, List<String>> ordersByStatus = orders.stream()
    .collect(Collectors.groupingBy(
        Order::getTrackingDetails,
        Collectors.mapping(Order::getOrderID, Collectors.toList())
    ));
```

---

## 🔁 Use Case 5: Group by Customer ID, then Filter + Sum Amounts

Filter out cancelled orders, group by customer, and sum amount.

```java
Map<String, Double> totalPerCustomer = orders.stream()
    .filter(order -> !order.isCancelled())
    .collect(Collectors.groupingBy(
        Order::extractCustomer,
        Collectors.summingDouble(Order::getAmount)
    ));
```

---

## 🔁 Use Case 6: `Optional` + `flatMap` for Filtering Safely

### Example: Convert List<Optional<User>> to List<User> (non-empty ones)

```java
List<Optional<String>> optList = List.of(
    Optional.of("Alice"),
    Optional.empty(),
    Optional.of("Bob")
);

List<String> names = optList.stream()
    .flatMap(Optional::stream)     // Java 9+ or use filter + map in Java 8
    .collect(Collectors.toList());

// Java 8 alternative
List<String> namesJava8 = optList.stream()
    .filter(Optional::isPresent)
    .map(Optional::get)
    .collect(Collectors.toList());
```

---

## 🔁 Use Case 7: Map of Lists → Flat Filter + Collection

```java
Map<String, List<String>> categoryToItems = Map.of(
    "fruits", List.of("apple", "banana"),
    "drinks", List.of("soda", "juice"),
    "junk", List.of("chips", "soda")
);

// Find all unique items excluding "soda"
Set<String> uniqueItems = categoryToItems.values().stream()
    .flatMap(List::stream)
    .filter(item -> !item.equals("soda"))
    .collect(Collectors.toSet());

System.out.println(uniqueItems); // [apple, banana, juice, chips]
```

---

## 💡 Tips for Combining These

| Goal | Suggestion |
|------|------------|
| Flatten nested lists | `.flatMap(List::stream)` |
| Combine map + filter | Do `filter()` first for performance |
| Want primitive sums/avg | Use `.mapToInt()`, `.mapToDouble()` |
| Need grouping + aggregation | Use `.groupingBy(..., downstream collector)` |
| Optional values in streams | Use `.flatMap(Optional::stream)` or `filter + map` |

---

Would you like these concepts **converted into utility functions** or **reusable helper methods** in your project? Or see similar examples in a domain like **orders/invoices**, **student-course** data, etc.?