package Hash;

// Java 7 --> segment based locking --> 16 segments --> smaller hashmaps
// Only the segment being written to or read from is locked
// read: do not require locking unless there is a write operation happening on the same segment
// write: lock

// java 8 --> no segmentation
//        --> Compare-And-Swap approach --> no locking except resizing or collision
// Thread A last saw --> x = 45
// Thread A work --> x to 50
// if x is still 45, then change it to 50 else don't change and retry
// put --> index

/*
ConcurrentHashMap (No Segmentation, CAS)
---------------------------------------------------
ConcurrentHashMap is designed for high-performance concurrent access. It doesn't
use segment-based locking in Java 8+, but rather a more fine-grained approach (often
described as using CAS operations and synchronized blocks for specific operations like
resizing or during heavy collisions).

wordCounts.compute(word, (key, count) -> { ... });: This is a powerful method for atomic
updates. Instead of get() followed by put() (which could lead to race conditions if not
synchronized externally), compute() performs the update in a single atomic operation.
The provided lambda function calculates the new value based on the current value. If the
key is not present, count will be null.

Concurrency in action: Multiple threads from the ExecutorService will concurrently try to
update the word counts in the wordCounts map. ConcurrentHashMap handles the internal
synchronization, ensuring data consistency without explicit locks around every put or get.
You might see output from different threads interleaved, but the final wordCounts will be
correct.

putIfAbsent: This method atomically adds a key-value pair only if the key is not already
present in the map.


*/

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentHashMapBasic {

    public static void main(String[] args) throws InterruptedException {

        ConcurrentHashMap<String, Integer> wordCounts = new ConcurrentHashMap<>();
        // Simulate multiple threads updating word counts
        ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicInteger totalWordsProcessed = new AtomicInteger(0);

        String[] words = {"apple", "banana", "apple", "orange", "banana", "apple", "grape", "orange", "apple"};

        for (String word : words) {
            executor.submit(() -> {
                // Using compute for atomic updates
                // This is a common pattern for ConcurrentHashMap to safely update values
                wordCounts.compute(word, (key, count) -> {
                    if (count == null) {
                        return 1;
                    } else {
                        return count + 1;
                    }
                });
                totalWordsProcessed.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + " processed: " + word);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES); // Wait for all tasks to complete

        System.out.println("\nFinal Word Counts: " + wordCounts);
        System.out.println("Total words processed by threads: " + totalWordsProcessed.get());

        // simple put/get (still thread-safe)
        wordCounts.put("kiwi", 10);
        System.out.println("Value of kiwi: " + wordCounts.get("kiwi"));

        // putIfAbsent
        wordCounts.putIfAbsent("apple", 100); // Will not change as 'apple' already exists
        System.out.println("Value of apple after putIfAbsent: " + wordCounts.get("apple"));
        wordCounts.putIfAbsent("melon", 1); // Will add 'melon'
        System.out.println("Value of melon: " + wordCounts.get("melon"));
    }
}
