Thread Synchronization
-------------------------------------------------------------------------------------
Thread synchronization is the process of coordinating the execution of multiple 
threads to prevent race conditions and ensure data consistency. It is crucial in 
multithreading to manage shared resources effectively.

---

### **Potential Issues in Thread Synchronization**
---------------------------------------------------------------------
1. **Deadlocks**  
   - Occurs when two or more threads are waiting for each other to release a lock, leading 
   to an infinite wait.

     class Deadlock {
         static class Resource {
             synchronized void methodA(Resource other) {
                 System.out.println(Thread.currentThread().getName() + " locked A");
                 try { Thread.sleep(100); } catch (InterruptedException e) {}
                 other.methodB();
             }

             synchronized void methodB() {
                 System.out.println(Thread.currentThread().getName() + " locked B");
             }
         }

         public static void main(String[] args) {
             Resource r1 = new Resource();
             Resource r2 = new Resource();

             Thread t1 = new Thread(() -> r1.methodA(r2));
             Thread t2 = new Thread(() -> r2.methodA(r1));

             t1.start();
             t2.start();
         }
     }

     - **Solution**: Use **lock ordering**, **timeouts**, or **deadlock detection 
     algorithms**.

2. **Livelocks**  
   - Similar to a deadlock but threads continuously change state in response to each 
   other without making progress.
   - Example: Two threads repeatedly try to acquire the same locks and release them 
   upon detecting contention.

3. **Thread Starvation**  
   - Occurs when low-priority threads never get CPU time because high-priority threads 
   keep executing.
   - Solution: **Fair scheduling policies** or **priority adjustments**.

---

## **Concurrency Utilities in Java**
-----------------------------------------------------------------------------------------
### **1. CountDownLatch**
------------------------------------------------------------------------------------
- A synchronization aid that allows threads to wait until a set of operations in other 
threads completes.

  import java.util.concurrent.CountDownLatch;

  public class CountDownLatchExample {
      public static void main(String[] args) throws InterruptedException {
          CountDownLatch latch = new CountDownLatch(3);

          Runnable task = () -> {
              System.out.println(Thread.currentThread().getName() + " completed");
              latch.countDown();
          };

          new Thread(task).start();
          new Thread(task).start();
          new Thread(task).start();

          latch.await(); // Main thread waits
          System.out.println("All tasks finished!");
      }
  }
  ```

### **2. CyclicBarrier**
-------------------------------------------------------------------------------------
- Similar to `CountDownLatch`, but it **resets** after all threads reach the barrier.

  import java.util.concurrent.CyclicBarrier;

  public class CyclicBarrierExample {
      public static void main(String[] args) {
          CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All threads reached the barrier!"));

          Runnable task = () -> {
              System.out.println(Thread.currentThread().getName() + " is waiting");
              try {
                  barrier.await();
              } catch (Exception e) {}
          };

          new Thread(task).start();
          new Thread(task).start();
          new Thread(task).start();
      }
  }
  ```

### **3. Semaphore**
-------------------------------------------------------------------------
- Controls access to a shared resource with a fixed number of permits.

  import java.util.concurrent.Semaphore;

  public class SemaphoreExample {
      public static void main(String[] args) {
          Semaphore semaphore = new Semaphore(2);

          Runnable task = () -> {
              try {
                  semaphore.acquire();
                  System.out.println(Thread.currentThread().getName() + " acquired permit");
                  Thread.sleep(2000);
                  System.out.println(Thread.currentThread().getName() + " releasing permit");
                  semaphore.release();
              } catch (InterruptedException e) {}
          };

          new Thread(task).start();
          new Thread(task).start();
          new Thread(task).start();
      }
  }
  ```

### 4. BlockingQueue Implementations
-----------------------------------------------------------------------
- Thread-safe queues such as:
  - `ArrayBlockingQueue`
  - `LinkedBlockingQueue`
  - `PriorityBlockingQueue`
  - `DelayQueue`


  import java.util.concurrent.ArrayBlockingQueue;

  public class BlockingQueueExample {
      public static void main(String[] args) {
          ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);

          new Thread(() -> {
              try {
                  queue.put(1);
                  System.out.println("Added 1");
                  queue.put(2);
                  System.out.println("Added 2");
                  queue.put(3); // Blocks since queue is full
              } catch (InterruptedException e) {}
          }).start();

          new Thread(() -> {
              try {
                  Thread.sleep(2000);
                  System.out.println("Removed: " + queue.take());
              } catch (InterruptedException e) {}
          }).start();
      }
  }


5. ThreadLocal
------------------------------------------------------------------------------------
- Provides thread-local variables, each thread gets its own isolated copy.

  public class ThreadLocalExample {
      private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

      public static void main(String[] args) {
          Runnable task = () -> {
              threadLocal.set(threadLocal.get() + 1);
              System.out.println(Thread.currentThread().getName() + " - " + threadLocal.get());
          };

          new Thread(task).start();
          new Thread(task).start();
      }
  }




- CountDownLatch: Used when tasks must wait for others to complete.
- CyclicBarrier: Synchronizes threads at a common point.
- Semaphore: Limits concurrent access to a resource.
- BlockingQueue: Safe thread communication.
- ThreadLocal: Provides thread-specific storage.

Not covered
===========================================================

Modern Java Concurrency
---------------------------------------------------------------------
Parallel Streams (Java 8)
CompletableFuture (Java 8)
Virtual Threads (Project Loom, introduced in JDK 21)
