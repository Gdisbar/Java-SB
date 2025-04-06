
🟠 1. What is the Java Collections Framework?
- A collection is a container that groups multiple objects into a single unit.  
- The Java Collections Framework provides:  
  ✅ Predefined data structures → List, Set, Map, Queue  
  ✅ Operations → Searching, sorting, insertion, deletion  
  ✅ Interfaces and Implementations → Unified architecture  
  ✅ Performance optimizations → Efficient memory and CPU usage,thread-safe variants for concurrency



🟠 2. Core Interfaces of Java Collections Framework
The Collection Framework defines several core interfaces:

| Interface | Description | Example Classes |
|-----------|-------------|-----------------|
| `Collection<E>` | Root interface | List, Set, Queue |
| `List<E>` | Ordered collection with duplicates allowed | ArrayList, LinkedList |
| `Set<E>` | Unordered collection with unique elements | HashSet, TreeSet |
| `Queue<E>` | Ordered collection following FIFO | PriorityQueue, LinkedList |
| `Deque<E>` | Double-ended queue | ArrayDeque, LinkedList |
| `Map<K, V>` | Key-value pairs | HashMap, TreeMap, LinkedHashMap |



✅ Collection Hierarchy 

               Iterable
                   |
               Collection
        ------------------------
        |          |           |
       List       Set         Queue
       |           |            |
ArrayList    HashSet     PriorityQueue
LinkedList   TreeSet     ArrayDeque
Vector       LinkedHashSet

                Map
        ----------------
        |              |
      HashMap        TreeMap
                     LinkedHashMap


🟠 3. List Interface 
A `List` is an ordered collection that allows duplicate elements. It provides positional 
access to elements and allows insertion in any position.

🔥 Methods in List Interface (inherited from Collection and extends List-specific methods):
--------------------------------------------------------------------------------------------
add(E e): Adds an element at the end.
add(int index, E element): Inserts an element at a specific index.
addAll(Collection<? extends E> c): Appends all elements from a collection.
addAll(int index, Collection<? extends E> c): Inserts all elements from a collection at a specific position.
clear(): Removes all elements from the list.
contains(Object o): Returns true if the list contains the element.
get(int index): Returns the element at the specified index.
indexOf(Object o): Returns the index of the first occurrence of an element.
lastIndexOf(Object o): Returns the index of the last occurrence of an element.
isEmpty(): Returns true if the list is empty.
iterator(): Returns an iterator over the elements.
listIterator(): Returns a list iterator over the elements.
listIterator(int index): Returns a list iterator starting from a specific index.
remove(int index): Removes the element at the specified index.
remove(Object o): Removes the first occurrence of the specified element.
removeAll(Collection<?> c): Removes all matching elements from the list.
retainAll(Collection<?> c): Retains only the elements present in the specified collection.
set(int index, E element): Replaces the element at a specified index.
size(): Returns the number of elements in the list.
subList(int fromIndex, int toIndex): Returns a view of the portion of the list between fromIndex and toIndex.
toArray(): Converts the list into an array.


import java.util.*;

public class ListInterfaceExample {
    public static void main(String[] args) {
        //Creating a List using ArrayList
        List<String> list = new ArrayList<>();
        
        // 🔹 add(E e): Adds elements to the list
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        System.out.println("List after add: " + list); // [Apple, Banana, Cherry]

        // 🔹 add(int index, E element): Insert at a specific index
        list.add(1, "Blueberry");
        System.out.println("List after insert: " + list); // [Apple, Blueberry, Banana, Cherry]

        // 🔹 addAll(Collection<? extends E> c): Add all elements from another collection
        List<String> moreFruits = Arrays.asList("Mango", "Orange");
        list.addAll(moreFruits);
        System.out.println("List after addAll: " + list); // [Apple, Blueberry, Banana, Cherry, Mango, Orange]

        // 🔹 get(int index): Get an element at a specific index
        System.out.println("Element at index 2: " + list.get(2)); // Banana

        // 🔹 indexOf(Object o): Get index of the first occurrence
        System.out.println("Index of 'Cherry': " + list.indexOf("Cherry")); // 3

        // 🔹 lastIndexOf(Object o): Get index of the last occurrence
        list.add("Apple"); // Duplicate element
        System.out.println("Last index of 'Apple': " + list.lastIndexOf("Apple")); // 6

        // 🔹 contains(Object o): Check if an element exists
        System.out.println("Contains 'Mango': " + list.contains("Mango")); // true

        // 🔹 isEmpty(): Check if the list is empty
        System.out.println("Is list empty? " + list.isEmpty()); // false

        // 🔹 size(): Get the number of elements
        System.out.println("List size: " + list.size()); // 7

        // 🔹 set(int index, E element): Replace element at index
        list.set(2, "Strawberry");
        System.out.println("List after set: " + list); // [Apple, Blueberry, Strawberry, Cherry, Mango, Orange, Apple]

        // 🔹 remove(int index): Remove element at index
        list.remove(4);
        System.out.println("List after remove(index): " + list); // [Apple, Blueberry, Strawberry, Cherry, Orange, Apple]

        // 🔹 remove(Object o): Remove first occurrence of an element
        list.remove("Apple");
        System.out.println("List after remove(Object): " + list); // [Blueberry, Strawberry, Cherry, Orange, Apple]

        // 🔹 subList(int fromIndex, int toIndex): Get a sublist
        List<String> subList = list.subList(1, 3);
        System.out.println("SubList(1,3): " + subList); // [Strawberry, Cherry]

        // 🔹 toArray(): Convert list to array
        String[] array = list.toArray(new String[0]);
        System.out.println("Array from list: " + Arrays.toString(array)); // [Blueberry, Strawberry, Cherry, Orange, Apple]

        // 🔹 iterator(): Get an iterator
        Iterator<String> iterator = list.iterator();
        System.out.print("Using Iterator: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " "); // Blueberry Strawberry Cherry Orange Apple
        }
        System.out.println();

        // 🔹 listIterator(): Get a list iterator
        ListIterator<String> listIterator = list.listIterator();
        System.out.print("Using ListIterator: ");
        while (listIterator.hasNext()) {
            System.out.print(listIterator.next() + " "); // Blueberry Strawberry Cherry Orange Apple
        }
        System.out.println();

        // 🔹 listIterator(int index): Start iteration from index
        ListIterator<String> reverseIterator = list.listIterator(list.size());
        System.out.print("Reverse order: ");
        while (reverseIterator.hasPrevious()) {
            System.out.print(reverseIterator.previous() + " "); // Apple Orange Cherry Strawberry Blueberry
        }
        System.out.println();

        // 🔹 removeAll(Collection<?> c): Remove all matching elements
        list.removeAll(Arrays.asList("Cherry", "Orange"));
        System.out.println("List after removeAll: " + list); // [Blueberry, Strawberry, Apple]

        // 🔹 retainAll(Collection<?> c): Keep only matching elements
        list.retainAll(Arrays.asList("Apple", "Blueberry"));
        System.out.println("List after retainAll: " + list); // [Blueberry, Apple]

        // 🔹 clear(): Remove all elements
        list.clear();
        System.out.println("List after clear(): " + list); // []
    }
}

🚀 Unique Behaviors of Vector and LinkedList in Java
------------------------------------------------------------------------------
1️⃣ Vector - Thread-Safe but Slower 🚦 
✅ Synchronized (Thread-Safe):  
- Unlike ArrayList, all methods in Vector are synchronized, making it safe for 
multi-threaded applications.  
- Downside: Synchronization introduces overhead, making it slower than ArrayList 
in single-threaded applications.  

✅ Legacy Collection:  
- Introduced in Java 1.0 (before ArrayList), but still used in multi-threaded environments.  

✅ Unique Methods in Vector: 
🔹 addElement(E obj): Adds an element (same as add()).  
🔹 capacity(): Returns the current capacity (internal storage size).  
🔹 ensureCapacity(int minCapacity): Increases capacity if needed.  
🔹 trimToSize(): Reduces capacity to match the number of elements.  
🔹 firstElement() & lastElement(): Get first or last element directly.  

🔥 Vector Unique Methods

import java.util.Vector;

public class VectorExample {
    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>(5); // Initial capacity 5

        vector.add(10);
        vector.add(20);
        vector.addElement(30); // Unique method

        System.out.println("Vector: " + vector); // [10, 20, 30]
        System.out.println("Capacity: " + vector.capacity()); // 5
        vector.trimToSize();
        System.out.println("Capacity after trimToSize: " + vector.capacity()); // 3
    }
}


🔹 Growth Mechanism: 
- ArrayList grows 50% when full.  
- Vector grows 100% (doubles in size) when full.  


2️⃣ LinkedList - Doubly Linked List 🔗
✅ Better for Frequent Insertions/Deletions:  
- Uses a doubly linked list, making add() and remove() operations faster than 
ArrayList (which shifts elements).  

✅ Implements Deque (Queue + Stack)  
- LinkedList is not just a List, it also implements Queue(Deque interface), 
making it a better choice for FIFO and LIFO operations.  

✅ Unique Methods in LinkedList:  
🔹 addFirst(E e) & addLast(E e): Add element at the head or tail.  
🔹 removeFirst() & removeLast(): Remove element from head or tail.  
🔹 getFirst() & getLast(): Retrieve element from head or tail.  
🔹 offerFirst(E e) & offerLast(E e): Similar to addFirst() but returns true/false.  
🔹 pollFirst() & pollLast(): Similar to removeFirst() but returns null if empty.  
🔹 peekFirst() & peekLast(): Similar to getFirst() but returns null if empty.  

🔥 LinkedList Unique Methods

import java.util.LinkedList;

public class LinkedListExample {
    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();

        linkedList.add("Apple");
        linkedList.add("Banana");
        linkedList.add("Cherry");

        linkedList.addFirst("Mango"); // Unique method
        linkedList.addLast("Orange"); // Unique method

        System.out.println("LinkedList: " + linkedList); // [Mango, Apple, Banana, Cherry, Orange]

        System.out.println("First Element: " + linkedList.getFirst()); // Mango
        System.out.println("Last Element: " + linkedList.getLast()); // Orange

        linkedList.removeFirst();
        linkedList.removeLast();
        System.out.println("LinkedList after removeFirst & removeLast: " + linkedList); // [Apple, Banana, Cherry]
    }
}


⚡ Performance Comparison
| Feature | `ArrayList` | `Vector` | `LinkedList` |
|---------|------------|----------|-------------|
| **Thread-Safety** | ❌ No | ✅ Yes (Synchronized) | ❌ No |
| **Performance** | 🚀 Fastest (Read-heavy operations) | 🐢 Slow (due to synchronization) | ⚡ Good (Frequent insert/delete) |
| **Memory Usage** | 📉 Less (uses a dynamic array) | 📉 Less | 📈 More (each node has extra references) |
| **Insert/Delete Performance** | ❌ Slow (O(n) for middle elements) | ❌ Slow (O(n) for middle elements) | ✅ Fast (O(1) at head/tail, O(n) in middle) |
| **Random Access (get())** | ✅ Fast (O(1)) | ✅ Fast (O(1)) | ❌ Slow (O(n)) |
| **Iteration Performance** | ✅ Fast | ❌ Slow (due to synchronization) | ⚡ Good (but more GC overhead) |
| **Special Features** | 🔹 Best for frequent **random access** | 🔹 Best for **thread safety** | 🔹 Best for **frequent insert/delete operations** |
| **Growth Mechanism** | 🛠 +50% growth when full | 🛠 Doubles capacity when full | 🛠 No resizing needed (linked structure) |

---

📌 When to Use Which?
| **Scenario** | **Best Choice** |
|-------------|---------------|
| **Fast Random Access (Read-heavy apps like Search Results)** | ✅ `ArrayList` |
| **Multi-threaded Applications** | ✅ `Vector` (or `Collections.synchronizedList(new ArrayList<>())`) |
| **Frequent Insert/Delete Operations (Streaming, Queues, etc.)** | ✅ `LinkedList` |
| **LIFO Stack Behavior** | ✅ `LinkedList` (Stack alternative) |
| **FIFO Queue Behavior** | ✅ `LinkedList` (Queue alternative) |



🎯 Conclusion
- **Use `ArrayList`** when you need **fast random access** and **read-heavy operations**.
- **Use `Vector`** when you need a **thread-safe `List`** (but it's slower due to synchronization).
- **Use `LinkedList`** when you need **fast insertions/deletions** (but it uses more memory).  


✅ 3.1 `ArrayList`  
- Implements dynamic array 
- Allows random access via an index  
- Fast for searching -> `add()` and `get()` are O(1) operations., 
slow for insertion/deletion  -> `remove()` is O(n) because shifting elements is required.  


✅ 3.2 `LinkedList` 
- Implements doubly linked list,Good for dynamic insertions and deletions  
- Fast for insertion and deletion ,`add()` and `remove()` are O(1) at the ends.
- Slow for random access  . Accessing an element is O(n).


✅ 3.3 `Vector` 
- Similar to `ArrayList` but thread-safe  
- Methods are synchronized  but slower 


🟠 4. Set Interface 
A `Set` is an unordered collection that does not allow duplicates.

import java.util.*;

public class SetInterfaceExample {
    public static void main(String[] args) {
        // Creating a Set using HashSet
        Set<String> set = new HashSet<>();

        // 🔹 add(E e): Adds elements to the set (ignores duplicates)
        set.add("Apple");
        set.add("Banana");
        set.add("Cherry");
        set.add("Apple"); // Duplicate, won't be added
        System.out.println("Set after add: " + set); // [Apple, Banana, Cherry] (Unordered)

        // 🔹 addAll(Collection<? extends E> c): Add all elements from another collection
        Set<String> moreFruits = new HashSet<>(Arrays.asList("Mango", "Orange"));
        set.addAll(moreFruits);
        System.out.println("Set after addAll: " + set); // [Apple, Banana, Cherry, Mango, Orange]

        // 🔹 contains(Object o): Check if an element exists
        System.out.println("Contains 'Mango': " + set.contains("Mango")); // true

        // 🔹 containsAll(Collection<?> c): Check if all elements exist
        System.out.println("Contains all [Banana, Cherry]: " + set.containsAll(Arrays.asList("Banana", "Cherry"))); // true

        // 🔹 isEmpty(): Check if the set is empty
        System.out.println("Is set empty? " + set.isEmpty()); // false

        // 🔹 size(): Get the number of elements
        System.out.println("Set size: " + set.size()); // 5

        // 🔹 remove(Object o): Remove an element
        set.remove("Banana");
        System.out.println("Set after remove('Banana'): " + set); // [Apple, Cherry, Mango, Orange]

        // 🔹 removeAll(Collection<?> c): Remove all matching elements
        set.removeAll(Arrays.asList("Mango", "Orange"));
        System.out.println("Set after removeAll: " + set); // [Apple, Cherry]

        // 🔹 retainAll(Collection<?> c): Keep only matching elements
        set.retainAll(Arrays.asList("Cherry", "Grapes"));
        System.out.println("Set after retainAll: " + set); // [Cherry]

        // 🔹 iterator(): Iterate through the set
        set.add("Kiwi");
        set.add("Pineapple");
        System.out.print("Using Iterator: ");
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " "); // Cherry Kiwi Pineapple
        }
        System.out.println();

        // 🔹 toArray(): Convert set to array
        // new String[0] - provide type hint & ensurereturned array is of type String[]
        String[] array = set.toArray(new String[0]);
        System.out.println("Array from set: " + Arrays.toString(array)); // [Cherry, Kiwi, Pineapple]

        // 🔹 clear(): Remove all elements
        set.clear();
        System.out.println("Set after clear(): " + set); // []
    }
}

✅ Why new String[0] instead of new String[set.size()]?
----------------------------------------------------------------
- If the array size (new String[0]) is smaller than the set's size, toArray() will 
internally create a new array of the required size.
If a larger array were passed (new String[set.size()]), it would be reused.
Using new String[0] is a best practice because it avoids unnecessary memory allocation.



### **🚀 TreeSet and LinkedHashSet - Unique Behavior**  

Both **`TreeSet`** and **`LinkedHashSet`** implement `Set`, but they introduce unique 
behaviors:  

✅ **`TreeSet`** maintains elements in **sorted order** (natural ordering or custom comparator).  
✅ **`LinkedHashSet`** maintains **insertion order**.  

🔥 Unique Methods in `TreeSet`  
`TreeSet` introduces some new methods for sorted set operations:  

1. **first()** → Returns the first (lowest) element.  
2. **last()** → Returns the last (highest) element.  
3. **headSet(E toElement)** → Returns elements less than `toElement`.  
4. **tailSet(E fromElement)** → Returns elements greater than or equal to `fromElement`.  
5. **subSet(E fromElement, E toElement)** → Returns elements in a specific range.  
6. **higher(E e)** → Returns the least element strictly greater than `e`.  
7. **lower(E e)** → Returns the greatest element strictly less than `e`.  
8. **ceiling(E e)** → Returns the least element **greater than or equal to** `e`.  
9. **floor(E e)** → Returns the greatest element **less than or equal to** `e`.  

🔥 `TreeSet` with Unique Methods**

import java.util.TreeSet;

public class TreeSetExample {
    public static void main(String[] args) {
        TreeSet<Integer> treeSet = new TreeSet<>();

        treeSet.add(10);
        treeSet.add(40);
        treeSet.add(20);
        treeSet.add(30);
        treeSet.add(50);

        System.out.println("TreeSet: " + treeSet); // [10, 20, 30, 40, 50]

        System.out.println("First element: " + treeSet.first()); // 10
        System.out.println("Last element: " + treeSet.last()); // 50
        System.out.println("HeadSet(30): " + treeSet.headSet(30)); // [10, 20]
        System.out.println("TailSet(30): " + treeSet.tailSet(30)); // [30, 40, 50]
        System.out.println("SubSet(20, 40): " + treeSet.subSet(20, 40)); // [20, 30]

        System.out.println("Higher(30): " + treeSet.higher(30)); // 40
        System.out.println("Lower(30): " + treeSet.lower(30)); // 20
        System.out.println("Ceiling(35): " + treeSet.ceiling(35)); // 40
        System.out.println("Floor(35): " + treeSet.floor(35)); // 30
    }
}

🔥 `LinkedHashSet` Unique Behavior
- LinkedHashSet preserves insertion order.
- It does not introduce new methods but behaves differently compared to `HashSet`.

🔥 LinkedHashSet 

import java.util.LinkedHashSet;

public class LinkedHashSetExample {
    public static void main(String[] args) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();

        linkedHashSet.add("B");
        linkedHashSet.add("A");
        linkedHashSet.add("D");
        linkedHashSet.add("C");

        System.out.println("LinkedHashSet (Insertion Order Preserved): " + linkedHashSet);
        // Output: [B, A, D, C]
    }
}

🛠 Summary
| Feature                 | `HashSet` | `TreeSet`         | `LinkedHashSet` |
|------------------------|------------|------------------|-----------------|
| **Duplicates**         | ❌ No     | ❌ No            | ❌ No           |
| **Order Maintained?** | ❌ No      | ✅ Sorted Order | ✅ Insertion Order |
| **Performance**  | 🔥 Fastest (O(1) for add/remove) | 🐢 Slower (O(log n) due to sorting) | 🚀 Slightly slower than `HashSet` |
| **Unique Methods?**  | ❌ No      | ✅ Yes (`first()`, `last()`, `higher()`, `lower()`, etc.) | ❌ No |


🟠 5. Queue Interface
A `Queue` stores elements in FIFO (First-In-First-Out) order.

import java.util.*;

public class QueueExample {
    public static void main(String[] args) {
        // Queue using LinkedList
        Queue<String> queue = new LinkedList<>();
        
        // 🔹 add(E e): Inserts an element into the queue (throws exception if fails)
        queue.add("Apple");
        queue.add("Banana");
        queue.add("Cherry");
        System.out.println("Queue after add: " + queue); // [Apple, Banana, Cherry]

        // 🔹 offer(E e): Inserts an element (returns false if fails)
        queue.offer("Date");
        System.out.println("Queue after offer: " + queue); // [Apple, Banana, Cherry, Date]

        // 🔹 remove(): Removes and returns head (throws exception if empty)
        System.out.println("Removed using remove(): " + queue.remove()); // Apple
        System.out.println("Queue after remove: " + queue); // [Banana, Cherry, Date]

        // 🔹 poll(): Removes and returns head (returns null if empty)
        System.out.println("Removed using poll(): " + queue.poll()); // Banana
        System.out.println("Queue after poll: " + queue); // [Cherry, Date]

        // 🔹 element(): Retrieves head (throws exception if empty)
        System.out.println("Head using element(): " + queue.element()); // Cherry

        // 🔹 peek(): Retrieves head (returns null if empty)
        System.out.println("Head using peek(): " + queue.peek()); // Cherry

        // Emptying the queue
        queue.clear();
        System.out.println("Queue after clear: " + queue); // []

        // Handling exceptions
        try {
            System.out.println(queue.element()); // Throws NoSuchElementException
        } catch (NoSuchElementException e) {
            System.out.println("element() throws exception on empty queue.");
        }

        System.out.println("peek() on empty queue: " + queue.peek()); // null
    }
}


Unique Methods in Implementations
1️⃣ PriorityQueue (Unique Methods)
- **Comparator<? super E> comparator()**: Returns the comparator used for ordering elements.
- **remove(Object o)**: Removes a single instance of the specified element.
- **iterator()**: Returns an iterator over elements in priority order.
- **spliterator()**: Returns a Spliterator over elements.


PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.add(20);
pq.add(10);
pq.add(30);
System.out.println("PriorityQueue: " + pq); // [10, 20, 30] (natural ordering)

System.out.println("Head using peek(): " + pq.peek()); // 10 (smallest element)
System.out.println("Head using poll(): " + pq.poll()); // 10 (removes the smallest)
System.out.println("PriorityQueue after poll: " + pq); // [20, 30]


---
### **2️⃣ LinkedList (When Used as a Queue - Unique Methods)**
Since `LinkedList` implements both `Queue` and `Deque`, it has extra methods:
- **getFirst(), getLast()**: Retrieves first/last element.
- **removeFirst(), removeLast()**: Removes first/last element.
- **offerFirst(E e), offerLast(E e)**: Inserts element at front/back.
- **pollFirst(), pollLast()**: Retrieves and removes first/last element.
- **peekFirst(), peekLast()**: Retrieves first/last element without removing.


LinkedList<String> linkedQueue = new LinkedList<>();
linkedQueue.add("One");
linkedQueue.add("Two");
linkedQueue.add("Three");

System.out.println("First Element: " + linkedQueue.getFirst()); // One
System.out.println("Last Element: " + linkedQueue.getLast()); // Three
linkedQueue.removeFirst();
System.out.println("After removeFirst(): " + linkedQueue); // [Two, Three]

### **3️⃣ ArrayDeque (Unique Methods)**
- **push(E e)**: Adds an element at the front.
- **pop()**: Removes and returns the first element.
- **offerFirst(E e), offerLast(E e)**: Inserts elements at front/back.
- **pollFirst(), pollLast()**: Retrieves and removes first/last element.
- **peekFirst(), peekLast()**: Retrieves first/last element without removing.


ArrayDeque<Integer> deque = new ArrayDeque<>();
deque.add(1);
deque.add(2);
deque.add(3);
System.out.println("ArrayDeque: " + deque); // [1, 2, 3]

deque.push(0);
System.out.println("After push(0): " + deque); // [0, 1, 2, 3]

System.out.println("Pop(): " + deque.pop()); // 0
System.out.println("After pop: " + deque); // [1, 2, 3]
```

### **Summary**
| **Implementation** | **Unique Features** |
|-------------------|-------------------|
| **PriorityQueue** | Elements ordered naturally or using a comparator, retrieves smallest element first. |
| **LinkedList (as Queue)** | Supports both `Queue` and `Deque` methods, allows insertion/removal from both ends. |
| **ArrayDeque** | More efficient than `LinkedList`, supports stack operations (`push`, `pop`). |

