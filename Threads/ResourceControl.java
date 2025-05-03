
/*
BlockingQueue Implementations
-----------------------------------------------------------------------
- Thread-safe queues such as:
  - `ArrayBlockingQueue`
  - `LinkedBlockingQueue`
  - `PriorityBlockingQueue`
  - `DelayQueue`


### 🔑 **Key Concepts Demonstrated**

Thread Coordination:

  * `Semaphore.acquire()` blocks if no permits are available.
  * `Semaphore.release()` returns a permit, increasing availability.
  * `BlockingQueue.put()` blocks if the queue is full.
  * `BlockingQueue.take()` blocks if the queue is empty.

acquired = Semaphore.acquire()

released = Semaphore.release()

BLOCKED = waiting on semaphore due to no available permits.

POISON_PILL is used to gracefully shut down the consumers.


---

### 🔧 Main Components

| Component             | Purpose                                                             |
| --------------------- | ------------------------------------------------------------------- |
| `Semaphore`           | Limits concurrent access to a resource (like DB connections).       |
| `LinkedBlockingQueue` | Acts as the shared queue for producer-consumer communication.       |
| `Producer Thread`     | Adds work items (`Task-0` to `Task-6`) to the queue.                |
| `Consumer Threads`    | Pull work from the queue and process using a limited resource pool. |

---

[Producer] ---> puts Task-0 ─────────────────────────┐
                                                     │
[Consumer 0] <--- takes Task-0                       ▼
              processing Task-0 --------> waiting for resource
                                        ---> acquired (permits left: 2)
                                                 │
                                                 ▼
                                        processing... (sleep)
                                                 │
                                        releases resource (permits: 3)
                                                 │
                                                 ▼
[Consumer 0] takes next task...

[Producer] ---> puts Task-1 ─────────────────────────┐
                                                     │
[Consumer 1] <--- takes Task-1                       ▼
              processing Task-1 --------> waiting for resource
                                        ---> acquired (permits left: 2)
                                                 ▼
                                        processing...

[Producer] ---> puts Task-2 ─────────────────────────┐
                                                     │
[Consumer 2] <--- takes Task-2                       ▼
              processing Task-2 --------> waiting for resource
                                        ---> acquired (permits left: 1)
                                                 ▼
                                        processing...

[Producer] ---> puts Task-3 ─────────────────────────┐
                                                     │
[Consumer 3] <--- takes Task-3                       ▼
              processing Task-3 --------> waiting for resource (BLOCKED)
                                                     ▲
[Producer] ---> puts Task-4 ─────────────────────────┘

[Consumer 2] finishes, releases permit (permits: 2)
   ▼
[Consumer 3] resumes, acquired resource (permits: 1)
   ▼
processing Task-3...

[Producer] ---> puts Task-5
[Producer] ---> puts Task-6

[Consumer 2] <--- takes Task-5
              processing Task-5 --------> waiting for resource
                                        ---> acquired

[Consumer 0] <--- takes Task-6
              processing Task-6 --------> waiting for resource

...

[Producer] ---> puts POISON_PILL x5

[Consumer 1] receives POISON_PILL ---> shutting down
[Consumer 3] finishes Task-3 ---> releases ---> shutting down
[Consumer 2] finishes Task-5 ---> releases ---> shuts down
[Consumer 4] finishes Task-4 ---> releases ---> shuts down
[Consumer 0] finishes Task-6 ---> releases ---> shuts down




*/

import java.util.concurrent.*;
import java.util.Random;

class ResourceControl {
    public static void main(String[] args) {
        // Limited resource pool (e.g., database connections, file handles)
        int MAX_AVAILABLE_RESOURCES = 3;
        
        // Semaphore - Control access to limited resources
        Semaphore resourcePool = new Semaphore(MAX_AVAILABLE_RESOURCES, true); // Fair semaphore
        
        // BlockingQueue - Thread-safe communication channel
        BlockingQueue<String> workQueue = new LinkedBlockingQueue<>(10); // Bounded queue with 10 capacity
        
        // Start producers (generate work)
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 7; i++) {
                    String task = "Task-" + i;
                    System.out.println("Producer created: " + task);
                    workQueue.put(task); // Will block if queue is full
                    Thread.sleep(200);
                }
                // Signal end of work with poison pills for all consumers
                for (int i = 0; i < 5; i++) {
                    workQueue.put("POISON_PILL");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Start consumers (process work with limited resources)
        Thread[] consumers = new Thread[5];
        for (int i = 0; i < consumers.length; i++) {
            final int consumerId = i;
            consumers[i] = new Thread(() -> {
                try {
                    while (true) {
                        String task = workQueue.take(); // Will block if queue is empty
                        
                        if (task.equals("POISON_PILL")) {
                            System.out.println("Consumer " + consumerId + " shutting down");
                            break;
                        }
                        
                        System.out.println("Consumer " + consumerId + " processing: " + task);
                        
                        // Acquire a resource permit before processing
                        System.out.println("Consumer " + consumerId + " waiting for resource...");
                        resourcePool.acquire();
                        try {
                            System.out.println("Consumer " + consumerId + " acquired resource, available: " + 
                                  resourcePool.availablePermits());
                            // Simulate task processing with the limited resource
                            Thread.sleep(1000 + new Random().nextInt(2000));
                        } finally {
                            // Always release the resource when done
                            resourcePool.release();
                            System.out.println("Consumer " + consumerId + " released resource, available: " + 
                                  resourcePool.availablePermits());
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            consumers[i].start();
        }
        
        producer.start();
        
        // Wait for all threads to complete
        try {
            producer.join();
            for (Thread consumer : consumers) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}