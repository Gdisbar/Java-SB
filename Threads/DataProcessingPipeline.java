/*

Key differences highlighted:
-----------------------------------------------
CountDownLatch - A synchronization aid that allows threads to wait until a set of 
                operations in other threads completes.
CyclicBarrier - Similar to `CountDownLatch`, but it resets after all threads reach the barrier.
CountDownLatch.countDown() decrements the counter while await() blocks until it reaches zero
CyclicBarrier.await() blocks until all threads reach the barrier, then all are released




 🧵 THREAD FLOW OF `DataProcessingPipeline` USING `CountDownLatch` AND `CyclicBarrier`


--- INITIALIZATION PHASE ------------------------------------

Main Thread
    |
    ├─> Launches 3 Worker Threads (Worker 0, Worker 1, Worker 2)
    |
    └─> Each worker does: 
            Print "Worker X initializing..."
            Sleep for random 1–3s
            Print "Worker X initialized"
            initLatch.countDown()       --> decrements latch from 3 to 0
            initLatch.await()           --> waits until all workers are initialized

                          ↓ Once latch reaches 0 ↓

--- PHASE 1: PROCESSING PHASE 1 --------------------------------

All Workers:
    ├─> Print "Worker X starting first processing phase"
    ├─> Sleep for 0.5–2s
    ├─> Print "Worker X completed first phase"
    └─> processingBarrier.await()       --> waits for all to reach barrier

Once all 3 workers hit barrier:
    └─> Barrier action: Print "All workers reached the barrier, moving to next phase!"

--- PHASE 2: PROCESSING PHASE 2 --------------------------------

All Workers:
    ├─> Print "Worker X starting second processing phase"
    ├─> Sleep for 0.5–2s
    ├─> Print "Worker X completed second phase"
    └─> processingBarrier.await()       --> again, wait for all to reach barrier

Once all 3 workers hit barrier again:
    └─> Barrier action: Print "All workers reached the barrier, moving to next phase!"

--- COMPLETION PHASE -------------------------------------------

All Workers:
    └─> Print "Worker X finished all processing"

Executor:
    └─> shutdown()


---

🧠 Summary of Synchronization Primitives Used

* CountDownLatch (one-time barrier): Used to ensure all workers complete 
            initialization before any processing starts.

* CyclicBarrier (reusable barrier): Used twice, once after the first phase and 
    again after the second, to synchronize all workers before moving to the next step.

How does processingBarrier.value decrease in other phases if we never decrement 
it like initLatch.countDown()?
    You do not manually decrement processingBarrier.
    Instead, every thread that reaches processingBarrier.await() is internally counted.
    Once all numberOfWorkers threads reach it, the barrier opens and resets automatically.

| Feature        | `CountDownLatch`               | `CyclicBarrier`                                    |
| -------------- | ------------------------------ | -------------------------------------------------- |
| Purpose        | One-time gate (countdown to 0) | Reusable gate — synchronize threads repeatedly     |
| How it works   | Threads **count down** to 0    | Threads **wait** until N have called `.await()`    |
| Resettable?    | ❌ No                           | ✅ Yes (automatically resets after N threads reach) |
| Barrier Action | ❌ No built-in action           | ✅ You can define an action to execute at barrier   |


*/

import java.util.concurrent.*;
import java.util.Random;

class DataProcessingPipeline {
    public static void main(String[] args) {
        int numberOfWorkers = 3;
        
        // CountDownLatch - Wait for initialization tasks to complete
        CountDownLatch initLatch = new CountDownLatch(numberOfWorkers);
        
        // CyclicBarrier - Synchronize workers at each processing phase
        CyclicBarrier processingBarrier = new CyclicBarrier(numberOfWorkers, 
            () -> System.out.println("All workers reached the barrier, moving to next phase!"));
        
        ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkers);
        
        for (int i = 0; i < numberOfWorkers; i++) {
            final int workerId = i; // inside executor.submit() all attributes are final
            executor.submit(() -> {
                try {
                    // Initialization phase
                    System.out.println("Worker " + workerId + " initializing...");
                    Thread.sleep(1000 + new Random().nextInt(2000)); // Simulate varied init times
                    System.out.println("Worker " + workerId + " initialized");
                    initLatch.countDown(); // Signal this worker is initialized
                    
                    // Wait for all workers to initialize before proceeding
                    initLatch.await();
                    System.out.println("Worker " + workerId + " starting first processing phase");
                    
                    // First processing phase
                    Thread.sleep(500 + new Random().nextInt(1500));
                    System.out.println("Worker " + workerId + " completed first phase");
                    
                    // Wait at barrier for all threads to complete first phase
                    processingBarrier.await();
                    
                    // Second processing phase
                    System.out.println("Worker " + workerId + " starting second processing phase");
                    Thread.sleep(500 + new Random().nextInt(1500));
                    System.out.println("Worker " + workerId + " completed second phase");
                    
                    // Wait at barrier again
                    processingBarrier.await();
                    
                    System.out.println("Worker " + workerId + " finished all processing");
                    
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        
        executor.shutdown();
    }
}
