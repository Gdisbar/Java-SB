### **Methods in Map Interface (Implemented by HashMap, TreeMap, and LinkedHashMap)**  
The `Map<K, V>` interface provides methods to store key-value pairs. Below are common methods inherited by all Map implementations.

---

### **Common Methods in Map Interface:**
| Method | Description |
|--------|------------|
| `put(K key, V value)` | Inserts or updates a key-value pair. |
| `putAll(Map<? extends K, ? extends V> m)` | Copies all mappings from another map. |
| `get(Object key)` | Retrieves the value associated with the key. |
| `containsKey(Object key)` | Returns `true` if the key exists. |
| `containsValue(Object value)` | Returns `true` if the value exists. |
| `remove(Object key)` | Removes the entry for the given key. |
| `size()` | Returns the number of key-value pairs. |
| `isEmpty()` | Returns `true` if the map has no elements. |
| `clear()` | Removes all entries. |
| `keySet()` | Returns a `Set<K>` of all keys. |
| `values()` | Returns a `Collection<V>` of all values. |
| `entrySet()` | Returns a `Set<Map.Entry<K, V>>` of all key-value pairs. |
| `getOrDefault(Object key, V defaultValue)` | Returns the value for a key or a default value if key is absent. |
| `replace(K key, V value)` | Replaces the value for a key if present. |
| `replace(K key, V oldValue, V newValue)` | Replaces value only if the existing value matches `oldValue`. |
| `merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)` | Merges the new value with an existing value using the provided function. |
| `compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)` | Computes a new value for the given key. |
| `computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)` | Computes a value only if key is absent. |
| `computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)` | Computes a new value only if the key is present. |

---

## **Unique Behaviors of HashMap, TreeMap, and LinkedHashMap**
| Feature | **HashMap** | **TreeMap** | **LinkedHashMap** |
|---------|-----------|------------|----------------|
| **Ordering** | No ordering (Unordered) | Sorted by **natural order** of keys (or a Comparator) | Insertion order maintained |
| **Null Keys** | **Allowed (only one)** | **Not allowed** | **Allowed (only one)** |
| **Null Values** | **Allowed** | **Allowed** | **Allowed** |
| **Performance (Average case)** | **O(1)** for get/put | **O(log n)** for get/put | **O(1)** for get/put |
| **Implementation** | Uses **hash table** | Uses **Red-Black tree** | Uses **hash table + doubly linked list** |
| **Iteration Order** | Random order | Sorted order | Order of insertion |

---

### **Additional Methods Unique to Each Implementation:**
#### **🔹 HashMap (No additional methods)**
- HashMap only provides standard Map methods.

#### **🔹 TreeMap (Extra Methods for Sorting)**
| Method | Description |
|--------|------------|
| `firstKey()` | Returns the first (lowest) key. |
| `lastKey()` | Returns the last (highest) key. |
| `headMap(K toKey)` | Returns a view of the map with keys **less than** `toKey`. |
| `tailMap(K fromKey)` | Returns a view of the map with keys **greater than or equal to** `fromKey`. |
| `subMap(K fromKey, K toKey)` | Returns a view of the map with keys in the given range. |
| `descendingKeySet()` | Returns a `NavigableSet<K>` in **reverse order**. |
| `descendingMap()` | Returns a `NavigableMap<K, V>` in **reverse order**. |
| `higherKey(K key)` | Returns the **next higher key** after `key`. |
| `lowerKey(K key)` | Returns the **next lower key** before `key`. |
| `ceilingKey(K key)` | Returns the **smallest key ≥ given key**. |
| `floorKey(K key)` | Returns the **largest key ≤ given key**. |

#### **🔹 LinkedHashMap (Extra Methods for Ordering)**
| Method | Description |
|--------|------------|
| `removeEldestEntry(Map.Entry<K, V> eldest)` | A protected method that can be overridden to **implement LRU (Least Recently Used) cache behavior**. |

---

### **Example Usage of Each Type:**
#### **1️⃣ HashMap Example**
```java
import java.util.*;

public class HashMapExample {
    public static void main(String[] args) {
        Map<Integer, String> hashMap = new HashMap<>();
        hashMap.put(3, "Three");
        hashMap.put(1, "One");
        hashMap.put(2, "Two");

        System.out.println("HashMap (unordered): " + hashMap);
        // Output may vary: {1=One, 2=Two, 3=Three} (order is not guaranteed)
    }
}
```

#### **2️⃣ TreeMap Example**
```java
import java.util.*;

public class TreeMapExample {
    public static void main(String[] args) {
        Map<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(3, "Three");
        treeMap.put(1, "One");
        treeMap.put(2, "Two");

        System.out.println("TreeMap (sorted): " + treeMap);
        // Output: {1=One, 2=Two, 3=Three} (sorted by key)
    }
}
```

#### **3️⃣ LinkedHashMap Example**
```java
import java.util.*;

public class LinkedHashMapExample {
    public static void main(String[] args) {
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(3, "Three");
        linkedHashMap.put(1, "One");
        linkedHashMap.put(2, "Two");

        System.out.println("LinkedHashMap (insertion order): " + linkedHashMap);
        // Output: {3=Three, 1=One, 2=Two} (order is preserved)
    }
}
```

---

### **When to Use Which?**
| **Use Case** | **Best Choice** |
|-------------|---------------|
| Need **fast lookups** (`O(1)`) and do not care about order | **HashMap** |
| Need **sorted keys** (`O(log n)`) | **TreeMap** |
| Need to **maintain insertion order** | **LinkedHashMap** |
| Need an **LRU cache** | **LinkedHashMap (Override `removeEldestEntry` method)** |

---

Let me know if you need further clarification! 🚀