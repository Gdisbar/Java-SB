/*

Key benefits of ThreadLocal illustrated:
------------------------------------------------------------------------
Provides thread-local variables, each thread gets its own isolated copy.
Maintains per-thread state without explicit parameter passing
Useful for transaction context, user authentication, or request tracking
The remove() method prevents memory leaks in thread pool environments
ThreadLocal.withInitial() simplifies initialization of thread-local values
Perfect for thread-safe use of non-thread-safe objects like SimpleDateFormat

- CountDownLatch: Used when tasks must wait for others to complete.
- CyclicBarrier: Synchronizes threads at a common point.
- Semaphore: Limits concurrent access to a resource.
- BlockingQueue: Safe thread communication.
- ThreadLocal: Provides thread-specific storage.



🧩 Legend
-> : Method call or task dispatch

--> : Internal execution or processing

x : Thread completes the task

[ ] : Context or Data

| : Life of a component (vertical timeline)



🧵 Text-Based Sequence Diagram

Main Thread     ExecutorService      Thread-1       Thread-2       Thread-3       Thread-4       Thread-5
    |                  |                  |              |              |              |              |
    |--submit(User 0)->|--assign task---->|              |              |              |              |
    |--submit(User 1)->|------------------|-->run(User 0)[TXN-0]        |              |              |
    |--submit(User 2)->|--------------------------------->run(User 1)[TXN-1]          |              |
    |--submit(User 3)->|------------------------------------------------->run(User 2)[TXN-2]        |
    |--submit(User 4)->|----------------------------------------------------------------->run(User 3)[TXN-3]
    |                  |--------------------------------------------------------------->run(User 4)[TXN-4]
    |                  |                  |              |              |              |              |
    |                  |                  |--set(ThreadLocal TXN-0)----->              |              |
    |                  |                  |-->simulateQuery(TXN-0)                     |              |
    |                  |                  |-->simulateUpdate(TXN-0)                    |              |
    |                  |                  |-->print complete TXN-0                     |              |
    |                  |                  |---------------------------x                |              |
    |                  |                  |              |-->simulateQuery(TXN-1)      |              |
    |                  |                  |              |-->simulateUpdate(TXN-1)     |              |
    |                  |                  |              |-->print complete TXN-1      |              |
    |                  |                  |              |-------------------x         |              |
    |                  |                  |              |              |-->simulateQuery(TXN-2)      |
    |                  |                  |              |              |-->simulateUpdate(TXN-2)     |
    |                  |                  |              |              |-->print complete TXN-2      |
    |                  |                  |              |              |-------------------x         |
    |                  |                  |              |              |              |-->Query(TXN-3)
    |                  |                  |              |              |              |-->Update(TXN-3)
    |                  |                  |              |              |              |-->complete    |
    |                  |                  |              |              |              |------------x  |
    |                  |                  |              |              |              |              |-->Query(TXN-4)
    |                  |                  |              |              |              |              |-->Update(TXN-4)
    |                  |                  |              |              |              |              |-->complete
    |                  |                  |              |              |              |              |-----x


*/

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.Random;

public class ThreadLocalUsage {
    // ThreadLocal - Thread-specific storage
    
    // Thread-specific transaction context
    private static class TransactionContext {
        private String transactionId;
        private long startTime;
        private int userId;
        
        public TransactionContext(int userId) {
            this.transactionId = "TXN-" + userId + "-" + System.nanoTime();
            this.startTime = System.currentTimeMillis();
            this.userId = userId;
        }

        @Override
        public String toString() {
            return String.format("Transaction[id=%s, user=%d, duration=%dms]", 
                transactionId, userId, System.currentTimeMillis() - startTime);
        }
    }
    
    // ThreadLocal storage for transaction context
    private static final ThreadLocal<TransactionContext> transactionContext = new ThreadLocal<>();
    
    // ThreadLocal for expensive-to-create objects like date formatters
    private static final ThreadLocal<SimpleDateFormat> dateFormatter = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for (int i = 0; i < 10; i++) {
            final int userId = i;
            executor.submit(() -> processUserRequest(userId));
        }
        
        executor.shutdown();
    }
    
    private static void processUserRequest(int userId) {
        try {
            // Initialize the thread-local transaction context
            transactionContext.set(new TransactionContext(userId));
            
            System.out.println(Thread.currentThread().getName() + " started " + transactionContext.get());
            
            // First service call
            queryDatabase();
            
            // Second service call
            updateRecord();
            
            // Log completion
            System.out.println(Thread.currentThread().getName() + " completed " + transactionContext.get());
            
        } finally {
            // Very important: clean up thread locals to prevent memory leaks
            transactionContext.remove();
            // No need to remove dateFormatter as it's reused across requests
        }
    }
    
    private static void queryDatabase() {
        // The transaction context is available without passing it explicitly
        TransactionContext ctx = transactionContext.get();
        
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Using the thread-local date formatter
        String timestamp = dateFormatter.get().format(new Date());
        System.out.println(timestamp + ": Query executed for " + ctx);
    }
    
    private static void updateRecord() {
        // Again, transaction context is available to this method without parameters
        TransactionContext ctx = transactionContext.get();
        
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String timestamp = dateFormatter.get().format(new Date());
        System.out.println(timestamp + ": Record updated for " + ctx);
    }
}

