package Hash;

// MAP --> SORTED --> THREAD SAFE --> ConcurrentSkipListMap
/*
ConcurrentSkipListMap Example (Sorted, Thread-Safe)
ConcurrentSkipListMap implements ConcurrentNavigableMap, meaning it's a concurrent,
sorted map. It's built on a "skiplist" data structure, which provides logarithmic time
complexity for most operations (insert, delete, search) while allowing for concurrent access.
It's particularly useful when you need sorted order and thread safety.


Concurrency with Sorted Order: Similar to ConcurrentHashMap, multiple threads
concurrently add entries to studentScores. The ConcurrentSkipListMap ensures that these
operations are thread-safe and that the map always maintains its sorted order by key, even
during concurrent modifications.
NavigableMap Features: ConcurrentSkipListMap provides all the rich features of the
NavigableMap interface:
firstEntry(), lastEntry(): Get the smallest and largest entries.
floorEntry(), ceilingEntry(): Find entries relative to a given key.
subMap(): Get a view of a portion of the map within a key range.
descendingMap(): Get a reverse-ordered view of the map.
These operations are also thread-safe.


* */
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentSkipListMapBasic {

    public static void main(String[] args) throws InterruptedException {

        ConcurrentSkipListMap<Integer, String> studentScores = new ConcurrentSkipListMap<>();

        // Simulate multiple threads adding student scores
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            final int score = (int) (Math.random() * 100) + 1; // Random score between 1 and 100
            final String studentName = "Student_" + i;

            executor.submit(() -> {
                studentScores.put(score, studentName);
                System.out.println(Thread.currentThread().getName() + " added: " + studentName + " with score " + score);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("\nFinal Student Scores (Sorted by Score):");
        for (Map.Entry<Integer, String> entry : studentScores.entrySet()) {
            System.out.println("Score: " + entry.getKey() + ", Student: " + entry.getValue());
        }

        // --- NavigableMap specific operations (thread-safe) ---

        // Get the first (lowest) entry
        Map.Entry<Integer, String> firstEntry = studentScores.firstEntry();
        System.out.println("\nLowest Score: " + firstEntry.getKey() + " (" + firstEntry.getValue() + ")");

        // Get the last (highest) entry
        Map.Entry<Integer, String> lastEntry = studentScores.lastEntry();
        System.out.println("Highest Score: " + lastEntry.getKey() + " (" + lastEntry.getValue() + ")");

        // Get the entry with a score less than or equal to 50
        Map.Entry<Integer, String> floorEntry = studentScores.floorEntry(50);
        if (floorEntry != null) {
            System.out.println("Score <= 50: " + floorEntry.getKey() + " (" + floorEntry.getValue() + ")");
        }

        // Get the entry with a score greater than or equal to 75
        Map.Entry<Integer, String> ceilingEntry = studentScores.ceilingEntry(75);
        if (ceilingEntry != null) {
            System.out.println("Score >= 75: " + ceilingEntry.getKey() + " (" + ceilingEntry.getValue() + ")");
        }

        // Get a sub-map (scores between 40 and 80, inclusive)
        System.out.println("\nScores between 40 and 80 (inclusive):");
        ConcurrentSkipListMap<Integer, String> subMap = new ConcurrentSkipListMap<>(studentScores.subMap(40, true, 80, true));
        for (Map.Entry<Integer, String> entry : subMap.entrySet()) {
            System.out.println("Score: " + entry.getKey() + ", Student: " + entry.getValue());
        }

        // Iterate in descending order
        System.out.println("\nScores in descending order:");
        Iterator<Map.Entry<Integer, String>> descendingIterator = studentScores.descendingMap().entrySet().iterator();
        while (descendingIterator.hasNext()) {
            Map.Entry<Integer, String> entry = descendingIterator.next();
            System.out.println("Score: " + entry.getKey() + ", Student: " + entry.getValue());
        }
    }
}