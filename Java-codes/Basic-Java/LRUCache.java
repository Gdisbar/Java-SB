import java.util.Map;
import java.util.LinkedHashMap;

class LRUCacheClass{
	private int capacity;
	private LinkedHashMap<Integer,Integer> cache;
	
	protected LRUCacheClass(int capacity){
		this.capacity = capacity;
		this.cache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
			@Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > LRUCacheClass.this.capacity;
            }
        };
	}
	protected int getKey(int key){
		return cache.getOrDefault(key, -1);
	}
	protected void setKey(int key,int value){
		cache.put(key, value);
	}
}

class LRUCache{
	public static void main(String[] args) {
		LRUCacheClass lruCache = new LRUCacheClass(2);

        lruCache.setKey(1, 1);
        lruCache.setKey(2, 2);
        System.out.println(lruCache.getKey(1)); // Output: 1
        lruCache.setKey(3, 3); // Evicts key 2
        System.out.println(lruCache.getKey(2)); // Output: -1 (not found)
        lruCache.setKey(4, 4); // Evicts key 1
        System.out.println(lruCache.getKey(1)); // Output: -1 (not found)
        System.out.println(lruCache.getKey(3)); // Output: 3
        System.out.println(lruCache.getKey(4)); //Output: 4
	}
}

/*
🔒 LRUCacheClass.this.capacity
This ensures the inner class refers to the outer class's capacity field (not its own or superclass’s)

@Override
protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest){}

By using an anonymous inner class to override the removeEldestEntry() method of LinkedHashMap.

🔁 This method is automatically called after every put() operation.

If it returns true, the eldest (least recently accessed) entry is removed.

So, whenever the size of the cache exceeds its capacity, the oldest entry 
(based on access order) is evicted.

new LinkedHashMap<>(capacity, 0.75f, true)

true: Access-order mode — this is key!

➡️ When true, the map maintains order of access, not just insertion.
So if you access an entry with get(), it moves to the end of the internal order.

This is essential for LRU: the first entry will always be the least recently used.

*/

/*

---

### 🔁 Common Use Cases of `Map.Entry<K, V>` (besides LRU Cache)

---

### 1. **Iterating through a `Map`**

✅ Most common use of `Map.Entry` is when you want to loop through the map and access both key and value:

```java
Map<Integer, String> studentMap = new HashMap<>();
studentMap.put(1, "Alice");
studentMap.put(2, "Bob");

for (Map.Entry<Integer, String> entry : studentMap.entrySet()) {
    System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue());
}
```

This is cleaner and faster than calling `map.get(key)` inside a loop.

---

### 2. **Sorting a Map by Value**

You can use `Map.Entry` to extract entries and then sort them:

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("A", 10);
scores.put("B", 30);
scores.put("C", 20);

List<Map.Entry<String, Integer>> entryList = new ArrayList<>(scores.entrySet());

entryList.sort(Map.Entry.comparingByValue());

for (Map.Entry<String, Integer> entry : entryList) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

---

### 3. **Creating a Custom Map Function (e.g., filtering)**

You can use it to filter a map:

```java
Map<String, Integer> inventory = Map.of("apple", 50, "banana", 30, "mango", 70);

Map<String, Integer> filtered = inventory.entrySet()
    .stream()
    .filter(e -> e.getValue() > 40)
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue
    ));
```

---

### 4. **Accessing Nested Maps**

```java
Map<String, Map<String, Integer>> data = new HashMap<>();

for (Map.Entry<String, Map<String, Integer>> outerEntry : data.entrySet()) {
    for (Map.Entry<String, Integer> innerEntry : outerEntry.getValue().entrySet()) {
        System.out.println("Outer Key: " + outerEntry.getKey() +
                           ", Inner Key: " + innerEntry.getKey() +
                           ", Value: " + innerEntry.getValue());
    }
}
```

---

### 5. **In Collections Framework Source Code**

`Map.Entry` is often used **under the hood** in Java Collections for things like:
- implementing `equals()` and `hashCode()` of maps
- merging maps
- customizing behavior in `TreeMap`, `LinkedHashMap`, `ConcurrentHashMap`, etc.

---

### 🔍 Summary: When to Use `Map.Entry<K, V>`

| Use Case | Why `Map.Entry` is useful |
|----------|---------------------------|
| Looping over map | Access key and value at once |
| Sorting by value/key | Needed for working with both |
| Filtering/map transformation | Functional-style operations |
| Nested data structures | Clean access to inner key-value pairs |
| Internal frameworks | Used in custom eviction or caching logic |

---




*/