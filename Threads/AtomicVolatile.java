/*

## 📦 Atomic Classes Overview (`java.util.concurrent.atomic`)

| Class                | Description                     | Key Methods                                                                                   |
| -------------------- | ------------------------------- | --------------------------------------------------------------------------------------------- |
| `AtomicInteger`      | For atomic `int` operations     | `get()`, `set()`, `getAndIncrement()`, `incrementAndGet()`, `compareAndSet(expected, update)` |
| `AtomicLong`         | For atomic `long` operations    | Same as `AtomicInteger`, but for `long`                                                       |
| `AtomicBoolean`      | For atomic `boolean` operations | `get()`, `set()`, `compareAndSet()`                                                           |
| `AtomicReference<V>` | For atomic object references    | `get()`, `set()`, `compareAndSet()`                                                           |

---

## 🧪 Example: `volatile` vs `AtomicInteger`

### 🔸 `volatile` Example (Only Visibility, Not Atomic)


public class VolatileExample {
    volatile boolean flag = false;

    public void writer() {
        flag = true;
    }

    public void reader() {
        if (flag) {
            System.out.println("Flag was set!");
        }
    }
}


📝 Usage: Ensures changes made by one thread to flags,state variables, timestamps 
that are visible to other threads. But it's not thread-safe for compound actions.

---

### 🔸 `AtomicInteger` Example (Atomic and Thread-safe)
-------------------------------------------------------------
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicExample {
    private AtomicInteger counter = new AtomicInteger(0);

    public void increment() {
        counter.incrementAndGet(); // atomically increments and returns new value
    }

    public int getValue() {
        return counter.get();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicExample example = new AtomicExample();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Final Counter: " + example.getValue()); // Should be 2000
    }
}


📝 Usage: Thread-safe increment operation using `AtomicInteger`. No need for 
explicit synchronization. Used for Counters, sequence numbers 

---

## ⚠️ volatile ≠ atomic
--------------------------------------------------------------------------
volatile int count = 0;
// Not atomic:
count++; // read-modify-write = NOT SAFE even if `count` is volatile
Use `AtomicInteger.incrementAndGet()` instead.

*/

/*

Atomic Variables:
--------------------------------------
AtomicInteger completedTaskCount - Used for counting completed tasks safely with 
concurrent increments
AtomicInteger errorCount - Used for counting errors with atomic operations


Volatile Variables:
----------------------------------
volatile boolean shutdownRequested - Flag to signal all threads to stop processing new tasks
volatile boolean paused - Flag to temporarily pause all processing
volatile long lastTaskCompletionTime - Ensures visibility of the latest task completion time


Starting to process 11 tasks using 4 threads
Progress: 2/11 tasks completed, 0 errors
Error : Task 0 : pool-1-thread-1 failed
Progress: 4/11 tasks completed, 1 errors
Progress: 6/11 tasks completed, 1 errors
Progress: 8/11 tasks completed, 1 errors
Pausing task processing...
Progress: 10/11 tasks completed, 1 errors

Processing complete!
Tasks completed: 10
Errors encountered: 1
Last task completed at: 40 seconds
Resuming task processing...
Requesting early shutdown...


random.nextDouble() < 0.1 - each task has a 10% chance of failure

// Pauses after 500ms : threads pause
Thread.sleep(500);
paused = true;

// Resumes after 1000ms
paused = false;

// If taskCount > 8, triggers shutdown after 1000ms : After ~2500ms
shutdownRequested = true;


*/
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.time.Instant;
import java.time.ZoneId;

class TaskProcessingFramework {
    // Atomic counter for tracking completed tasks (needs atomicity for incrementing)
    private final AtomicInteger completedTaskCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);
    
    // Volatile flag for signaling process state changes (only needs visibility, not atomicity)
    private volatile boolean shutdownRequested = false;
    private volatile boolean paused = false;
    
    // Tracking task execution time
    private volatile long lastTaskCompletionTime = 0;
    
    public void processTaskBatch(int taskCount) throws InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        CountDownLatch latch = new CountDownLatch(taskCount);
        Random random = new Random();
        
        System.out.println("Starting to process " + taskCount + " tasks using " + processors + " threads");
        
        // Start task processing
        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    while (paused) {
                        // Check for pause flag (uses volatile's visibility guarantee)
                        Thread.sleep(100);
                    }
                    
                    if (shutdownRequested) {
                        // Fast termination using volatile flag
                        System.out.println("Task " + taskId + " : "
                            +Thread.currentThread().getName() + " skipped due to shutdown");
                        return;
                    }
                    
                    // Simulate task processing
                    Thread.sleep(50 + random.nextInt(200));
                    
                    // Simulate occasional task failure
                    if (random.nextDouble() < 0.1) {
                        throw new RuntimeException("Task " + taskId 
                            + " : "+Thread.currentThread().getName() + " failed");
                    }
                    
                    // Atomic increment of completed counter
                    int completed = completedTaskCount.incrementAndGet();
                    
                    // Update volatile last completion time
                    lastTaskCompletionTime = System.currentTimeMillis();
                    
                    // Log every 10th task
                    if (completed % 2 == 0) {
                        System.out.println("Progress: " + completed + "/" + taskCount + 
                                          " tasks completed, " + errorCount.get() + " errors");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    // Atomic increment of error counter
                    errorCount.incrementAndGet();
                    System.out.println("Error : "+e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Simulate external control commands
        new Thread(() -> {
            try {
                // Pause processing after 500ms
                Thread.sleep(500);
                System.out.println("Pausing task processing...");
                paused = true;
                
                Thread.sleep(1000);
                System.out.println("Resuming task processing...");
                paused = false;
                
                if (taskCount > 8) {
                    // Simulate early shutdown request for large batches
                    Thread.sleep(1000);
                    System.out.println("Requesting early shutdown...");
                    shutdownRequested = true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        // Wait for all tasks to complete or be skipped
        latch.await();
        executor.shutdown();
        
        // Final statistics
        System.out.println("\nProcessing complete!");
        System.out.println("Tasks completed: " + completedTaskCount.get());
        System.out.println("Errors encountered: " + errorCount.get());
        System.out.println("Last task completed at: " 
            + Instant.ofEpochMilli(lastTaskCompletionTime)
                            .atZone(ZoneId.systemDefault()).getSecond()+ " seconds");
    }
}

class AtomicVolatile{

    public static void main(String[] args) {
        try {
            TaskProcessingFramework framework = new TaskProcessingFramework();
            framework.processTaskBatch(11);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}