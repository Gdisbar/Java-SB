/*

## Problem 2: Generic Cache with LRU Eviction Strategy

**Problem Statement:**
Design and implement a generic cache with a Least Recently Used (LRU) eviction policy. The cache should be thread-safe and efficiently handle concurrent access.

**Requirements:**
1. Create a generic `LRUCache<K, V>` class with configurable capacity
2. Implement `get(K key)`, `put(K key, V value)`, and `remove(K key)` operations
3. When the cache reaches capacity, the least recently used item should be evicted
4. All operations should be thread-safe
5. Provide O(1) time complexity for common operations
6. Include statistics tracking (hits, misses, evictions)

**Expected Solution Approach:**
Use a combination of `ConcurrentHashMap` and a synchronized double-linked list to track the access order, or consider Java's `LinkedHashMap` with proper synchronization.


class LRUCache<K, V> {
        - capacity: int
        - cacheMap: ConcurrentHashMap<K, Node<K, V>>
        - head: Node<K, V> (dummy, MRU)
        - tail: Node<K, V> (dummy, LRU)
        - lock: ReentrantLock
        - hits: AtomicLong
        - misses: AtomicLong
        - evictions: AtomicLong
        + LRUCache(capacity: int)
        + get(key: K): V
        + put(key: K, value: V): void
        + remove(key: K): void
        + getHits(): long
        + getMisses(): long
        + getEvictions(): long
    }

    class Node<K, V> {
        + key: K
        + value: V
        + prev: Node<K, V>
        + next: Node<K, V>
        + Node(key: K, value: V)
    }

    LRUCache "1" *-- "0..*" Node : contains
    LRUCache --> ConcurrentHashMap : uses
    LRUCache --> ReentrantLock : uses
    LRUCache --> AtomicLong : uses
    Node "1" <--> "1" Node : prev/next


* */

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

class Node<K,V>{
    private K key;
    private V value;
    private Node<K,V> prev;
    private Node<K,V> next;
    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key;}
    public V getValue() {return value;}
    public void setValue(Node<K,V> node,V value){ this.value = value;}

    public Node<K, V> getPrev() {return prev;}
    public Node<K, V> getNext() {return next;}
    public void setPrev(Node<K, V> prev) { this.prev = prev; }
    public void setNext(Node<K, V> next) { this.next = next; }
}

class LRUCache<K,V>{
    private int capacity;
    private ConcurrentHashMap<K, Node<K, V>> cacheMap;
    protected Node<K, V> head;
    protected Node<K, V> tail;
    ReentrantLock lock = new ReentrantLock();
    AtomicLong hits = new AtomicLong(0);
    AtomicLong misses = new AtomicLong(0);
    AtomicLong evictions = new AtomicLong(0);

    public LRUCache(int capacity){
        this.capacity = capacity;
        this.cacheMap = new ConcurrentHashMap<>(capacity);
        this.head = new Node<>(null, null); // Using no-arg constructor now
        this.tail = new Node<>(null, null); // Using no-arg constructor now
        head.setNext(tail);
        tail.setPrev(head);
    }

    private void addNode(Node<K, V> node) {
//        node.next = head.next;
//        node.prev = head;
//        head.next.prev = node;
//        head.next = node;
        node.setNext(head.getNext());
        node.setPrev(head);
        head.getNext().setPrev(node); // The old first node now points back to new node
        head.setNext(node);
    }

    private void removeNode(Node<K, V> node) {
//        node.prev.next = node.next;
//        node.next.prev = node.prev;
//        node.prev = null;
//        node.next = null;
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        node.setPrev(null);
        node.setNext(null);
    }

    private Node<K, V> removeLRU() {
        Node<K, V> lruNode = tail.getPrev();
        removeNode(lruNode);
        return lruNode;
    }


    public V get(K key){
        lock.lock();
        try {
            Node<K, V> node = cacheMap.get(key);
            if (node == null) {
                misses.incrementAndGet();
                return null;
            }
            hits.incrementAndGet();
            // Move the accessed node to the head (MRU position)
            removeNode(node); // Remove from its current position
            addNode(node);    // Add to the head
            return node.getValue();
        } finally {
            lock.unlock();
        }
    }
    public void put(K key,V value){
        lock.lock();
        try {
            Node<K, V> existingNode = cacheMap.get(key);
            if (existingNode != null) {
                // Key already exists, update value and move to head
                existingNode.setValue(existingNode,value);
                removeNode(existingNode);
                addNode(existingNode);
            } else {
                // New key
                if (cacheMap.size() >= capacity) { // Cache is full, evict LRU
                    Node<K, V> lruNode = removeLRU(); // Correctly call the helper method
                    cacheMap.remove(lruNode.getKey());
                    evictions.incrementAndGet();
                }
                Node<K, V> newNode = new Node<>(key, value);
                cacheMap.put(key, newNode);
                addNode(newNode); // Add new node to head
            }
        } finally {
            lock.unlock();
        }
    }
    public void remove(K key){
        lock.lock();
        try {
            Node<K, V> nodeToRemove = cacheMap.remove(key);
            if (nodeToRemove != null) {
                removeNode(nodeToRemove); // Correctly call the helper method
            }
        } finally {
            lock.unlock();
        }
    }

    public long getHits() { // Return long, not AtomicLong object
        return hits.get();
    }

    public long getMisses() { // Return long, not AtomicLong object
        return misses.get();
    }

    public long getEvictions() { // Return long, not AtomicLong object
        return evictions.get();
    }
    public void printCacheState() {
        lock.lock();
        try {
            System.out.print("Cache State (MRU -> LRU): [");
            Node<K, V> current = head.getNext();
            while (current != tail) {
                System.out.print(current.getKey() + ":" + current.getValue());
                current = current.getNext();
                if (current != tail) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        } finally {
            lock.unlock();
        }
    }
}


public class GenericCacheLRU {
    public static void main(String[] args) {
        System.out.println("--- Demonstrating LRU Cache ---");

        // 1. Create an LRUCache instance with capacity 3
        LRUCache<String, String> cache = new LRUCache<>(3);

        System.out.println("\n--- Initial Puts ---");
        cache.put("key1", "value1"); // Cache: [key1]
        cache.printCacheState();
        cache.put("key2", "value2"); // Cache: [key2, key1]
        cache.printCacheState();
        cache.put("key3", "value3"); // Cache: [key3, key2, key1]
        cache.printCacheState();

        System.out.println("\n--- Get Operations ---");
        System.out.println("Get key2: " + cache.get("key2")); // Hit, key2 becomes MRU. Cache: [key2, key3, key1]
        cache.printCacheState();
        System.out.println("Get key1: " + cache.get("key1")); // Hit, key1 becomes MRU. Cache: [key1, key2, key3]
        cache.printCacheState();
        System.out.println("Get nonExistentKey: " + cache.get("nonExistentKey")); // Miss. Cache state unchanged.
        cache.printCacheState();

        System.out.println("\n--- Eviction Demonstration (Capacity 3) ---");
        // Adding a new item will evict "key3" (LRU)
        cache.put("key4", "value4"); // Evicts key3. Cache: [key4, key1, key2]
        cache.printCacheState();
        System.out.println("Get key3 (should be null): " + cache.get("key3")); // Miss, as key3 was evicted
        cache.printCacheState();

        System.out.println("\n--- Update Existing Item ---");
        cache.put("key1", "updatedValue1"); // Update key1, it moves to MRU. Cache: [key1, key4, key2]
        cache.printCacheState();
        System.out.println("Get key1 (updated): " + cache.get("key1"));
        cache.printCacheState();

        System.out.println("\n--- Remove Operation ---");
        cache.remove("key2"); // Remove key2. Cache: [key1, key4]
        cache.printCacheState();
        System.out.println("Get key2 (should be null): " + cache.get("key2")); // Miss
        cache.printCacheState();

        cache.put("key5", "value5"); // Cache: [key5, key1, key4]
        cache.printCacheState();
        cache.put("key6", "value6"); // Evicts key4. Cache: [key6, key5, key1]
        cache.printCacheState();

        System.out.println("\n--- Final Statistics ---");
        System.out.println("Hits: " + cache.getHits());
        System.out.println("Misses: " + cache.getMisses());
        System.out.println("Evictions: " + cache.getEvictions());

        System.out.println("\n--- Demonstrating Thread-Safety ---");
        LRUCache<Integer, String> concurrentCache = new LRUCache<>(5);
        ExecutorService executor = Executors.newFixedThreadPool(10); // 10 threads

        // Task to put and get items concurrently
        Runnable task = () -> {
            for (int i = 0; i < 20; i++) {
                int key = (int) (Math.random() * 10); // Keys 0-9
                String value = "Value-" + Thread.currentThread().getId() + "-" + i;
                concurrentCache.put(key, value);
                try {
                    Thread.sleep((long) (Math.random() * 10)); // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                concurrentCache.get(key); // Get the same key
                concurrentCache.get((int) (Math.random() * 15)); // Get a random key, might be a miss
            }
        };

        for (int i = 0; i < 10; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        try {
            // Wait for all tasks to complete or timeout after 30 seconds
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Tasks did not finish in time.");
            }
        } catch (InterruptedException e) {
            System.err.println("Executor shutdown interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- Concurrent Cache Final Statistics ---");
        System.out.println("Concurrent Cache Hits: " + concurrentCache.getHits());
        System.out.println("Concurrent Cache Misses: " + concurrentCache.getMisses());
        System.out.println("Concurrent Cache Evictions: " + concurrentCache.getEvictions());
        concurrentCache.printCacheState();

        System.out.println("\n--- End of Demonstration ---");
    }
}
