/*
PARALLEL URL PROCESSING EXAMPLE
--------------------------------------------------
Processing 8 URLs in parallel...
✓ Data from https://example.com/api/1 (took 1797ms, thread: pool-1-thread-1)
✗ Failed to process https://example.com/api/2: Connection timeout for https://example.com/api/2
✓ Data from https://example.com/api/3 (took 1450ms, thread: pool-1-thread-3)
✓ Data from https://example.com/api/4 (took 1770ms, thread: pool-1-thread-4)
✓ Data from https://example.com/api/5 (took 1357ms, thread: pool-1-thread-2)
✓ Data from https://example.com/api/6 (took 1090ms, thread: pool-1-thread-3)
✓ Data from https://example.com/api/7 (took 1771ms, thread: pool-1-thread-4)
✓ Data from https://example.com/api/8 (took 1800ms, thread: pool-1-thread-1)

Processing complete:
Successful requests: 7
Failed requests: 1

*/

import java.util.concurrent.*;
import java.util.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

class ParallelUrlProcessing {
    public static void main(String[] args) {
        System.out.println("PARALLEL URL PROCESSING EXAMPLE");
        System.out.println("-".repeat(50));
        
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        try {
            // Create a collection of URLs to process
            List<String> urls = Arrays.asList(
                "https://example.com/api/1",
                "https://example.com/api/2",
                "https://example.com/api/3",
                "https://example.com/api/4",
                "https://example.com/api/5",
                "https://example.com/api/6",
                "https://example.com/api/7",
                "https://example.com/api/8"
            );
            
            System.out.println("Processing " + urls.size() + " URLs in parallel...");
            
            // Create a task for each URL
            List<Callable<String>> tasks = new ArrayList<>();
            for (String url : urls) {
                tasks.add(() -> fetchData(url));
            }
            
            // Submit all tasks at once
            List<Future<String>> futures = executor.invokeAll(tasks);
            
            // Process results as they become available
            int successCount = 0;
            int failureCount = 0;
            
            for (int i = 0; i < futures.size(); i++) {
                try {
                    String result = futures.get(i).get();
                    System.out.println("✓ " + result);
                    successCount++;
                } catch (ExecutionException e) {
                    System.err.println("✗ Failed to process " + urls.get(i) + ": " + 
                                      e.getCause().getMessage());
                    failureCount++;
                }
            }
            
            System.out.println("\nProcessing complete:");
            System.out.println("Successful requests: " + successCount);
            System.out.println("Failed requests: " + failureCount);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
    
    /**
     * Simulates fetching data from a URL with random delays and occasional failures.
     */
    private static String fetchData(String url) throws Exception {
        String threadName = Thread.currentThread().getName();
        
        // Simulate network delay
        long delay = (long)(Math.random() * 2000);
        Thread.sleep(delay);
        
        // Simulate occasional failures
        if (Math.random() < 0.2) {
            throw new IOException("Connection timeout for " + url);
        }
        
        return "Data from " + url + " (took " + delay + "ms, thread: " + threadName + ")";
    }
}
