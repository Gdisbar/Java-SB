Process (Running two instances of a browser)
-----------------------------------------------------
Process is a program in execution.Each process has its own memory space
Processes communicate using IPC (Inter-Process Communication)

Thread (Opening two tabs in a browser)
---------------------------------------------------------
A lightweight subtask within a process, Threads within a process share memory
Threads communicate using shared memory

Java provides robust support for multithreading through the 
java.lang.Thread class, Runnable interface, Executor Framework, and various concurrent utilities.

// Start a new process using ProcessBuilder
try {
    Process process = new ProcessBuilder("notepad.exe").start();
    System.out.println("Process started");
} catch (Exception e) {
    e.printStackTrace();
}

Creating Threads
------------------------------------------------------------------
Extending the Thread class
------------------------------------------------

class Task extends Thread {
    public void run() { //Override the run() method
        for (int i = 1; i <= 5; i++) {
            System.out.println(Thread.currentThread().getName() + " - Task: " + i);
        }
    }
}
//Create an instance of the class and call start() to begin execution.
Task task1 = new Task();
Task task2 = new Task();

task1.start(); // Start thread 1
task2.start(); // Start thread 2

// Thread-0 - Task: 1
// Thread-1 - Task: 1
// Thread-0 - Task: 2
// Thread-1 - Task: 2

Implementing the Runnable Interface (Runnable does not return a result,allows multiple inheritance.)
---------------------------------------------------------------------------------------------------
class MyRunnable implements Runnable {
    public void run() { 
        System.out.println("Thread running using Runnable");
    }
}
//Create a Runnable object and pass it to a Thread instance.
MyRunnable task = new MyRunnable();
Thread t = new Thread(task);
t.start();

Using Callable and Future
-------------------------------------------------------------------------
- Callable returns a result and can throw exceptions.
- Future represents the result of an asynchronous computation.

import java.util.concurrent.*;

class MyCallable implements Callable<String> {
    public String call() throws Exception {
        return "Task Complete!";
    }
}

ExecutorService executor = Executors.newFixedThreadPool(1);
Future<String> future = executor.submit(new MyCallable());

System.out.println(future.get()); // Future.get() blocks until the result is available
executor.shutdown();


Single-threaded vs Multithreaded Execution
-----------------------------------------------------------------------
Single-threaded -> One task is executed at a time
Multithreaded   -> Multiple tasks are executed concurrently
Parallelism     -> True simultaneous execution (multiple processors)
Concurrency     -> Tasks are managed in an overlapping time frame

Why Multithreading is Needed?
✔️ Efficient CPU utilization
✔️ Improved performance
✔️ Better resource sharing
✔️ Faster response times
✔️ Handling large I/O-bound operations

Thread States
----------------------------------------------------------------------
NEW        -> Created but not yet started (start() not called).
RUNNABLE   -> Ready to run or currently executing.
BLOCKED    -> Waiting for a lock.
WAITING    -> Waiting indefinitely for another thread to notify it.
TIMED_WAITING   ->Waiting for a specified time (sleep, join, wait).
TERMINATED      ->Finished execution.


class MyThread extends Thread {
    public void run() {
        System.out.println("Thread is running");
        try {
            Thread.sleep(1000);
            System.out.println("Thread woke up");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

MyThread t = new MyThread();
System.out.println(t.getState()); // NEW
t.start();
System.out.println(t.getState()); // RUNNABLE
Thread.sleep(500);
System.out.println(t.getState()); // TIMED_WAITING
t.join();
System.out.println(t.getState()); // TERMINATED

join()
---------------------------------------------------------------------------------
Thread thread1 = new Thread(() -> {
    System.out.println("Thread 1 started");
    try {
        Thread.sleep(2000); // Simulate some work
    } catch (InterruptedException e) {e.printStackTrace();}
    System.out.println("Thread 1 finished");
});

Thread thread2 = new Thread(() -> {
    System.out.println("Thread 2 started");
    try {
        thread1.join(); // Thread 2 waits for Thread 1 to finish
    } catch (InterruptedException e) {e.printStackTrace();}
    System.out.println("Thread 2 finished after Thread 1");
});

thread1.start();
thread2.start();

System.out.println("Main thread continues");

try {
    thread2.join(); // Main thread waits for thread2 to finish.
} catch (InterruptedException e) {e.printStackTrace();}

System.out.println("Main thread finished after thread 2");

// Main thread continues
// Thread 1 started
// Thread 2 started
// Thread 1 finished
// Thread 2 finished after Thread 1
// Main thread finished after thread 2

Thread Synchronization
--------------------------------------------------------------------
When multiple threads access shared data simultaneously → Race conditions occur.
Synchronization ensures that only one thread accesses the critical section at a time.

Synchronized Method (Only one thread can access a synchronized method at a time.)
----------------------------------------------------------------------------------
class Shared {
    synchronized void display() {
        System.out.println(Thread.currentThread().getName() + " is executing");
    }
}

class MyThread extends Thread {
    Shared shared;
    MyThread(Shared shared) {
        this.shared = shared;
    }
    public void run() {
        shared.display();
    }
}


Synchronized Block (Allows finer control over the lock)
----------------------------------------------------------------------------
class Shared {
    void display() {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + " is executing");
        }
    }
}


ReentrantLock
-----------------------------------------------------------------------------
More flexible than synchronized
acquiring lock without blocking -> tryLock() and 
interrupting a thread waiting for a lock ->lockInterruptibly() options.

import java.util.concurrent.locks.*;

class Shared {
    private final ReentrantLock lock = new ReentrantLock();
    
    void display() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is executing");
        } finally {
            lock.unlock();  // Always release lock in finally block
        }
    }
}

Volatile Keyword
------------------------------------------------------------------------------
The volatile keyword ensures visibility of changes to variables across threads.

class SharedCounter {
    private volatile int count = 0;  // Changes to count are visible to all threads
    
    void increment() {
        count++;  // Not atomic!
    }
    
    int getCount() {
        return count;
    }
}


Atomic Variables/Classes
------------------------------------------
An atomic operation is an operation that is performed as a single, indivisible unit. 
It cannot be interrupted or broken down into smaller steps. In the context of 
multithreaded programming, this means that when an atomic operation is performed on 
a shared variable, no other thread can observe or modify the variable while the 
operation is in progress. 

atomic variables in the java.util.concurrent.atomic package for performing atomic 
operations without locks

AtomicInteger, AtomicLong, AtomicBoolean
AtomicReference<V> for reference types
AtomicIntegerArray, AtomicLongArray, AtomicReferenceArray<V>

import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeCounter {
    private AtomicInteger count = new AtomicInteger(0);
    
    void increment() {
        count.incrementAndGet();  // Atomic operation
    }
    
    int getCount() {
        return count.get();
    }
}


Best Practices
----------------------------------
✅ Minimize the scope of synchronized blocks.
✅ Avoid holding locks for too long.
✅ Use ExecutorService over manual thread creation.
✅ Handle InterruptedException properly.
✅ Use ConcurrentHashMap instead of Hashtable.
✅ Use lock ordering to prevent deadlocks
✅ Release locks in finally blocks
✅ Consider higher-level concurrency utilities when appropriate
✅ Use thread-safe collections from java.util.concurrent package
✅ Prefer atomic variables for simple counters and flags
✅ Avoid nested locks when possible

Thread Pools
-------------------------------------------------------------------------
A Thread Pool is a collection of worker threads that are created once and reused to execute 
multiple tasks.

Managed by the Executor Framework (java.util.concurrent) introduced in Java 5.
Thread pools manage a queue of tasks, assigning them to available threads.

Why Use Thread Pools?
✔️ Creating threads is expensive → Reduces overhead.
✔️ Avoids creating too many threads → Prevents CPU thrashing.
✔️ Efficient task scheduling and management.
✔️ Allows tuning the number of threads based on workload.

How Thread Pools Work Internally
---------------------------------------------------------------------
-A fixed number of worker threads are created.
-Tasks are submitted to the pool via execute() or submit() methods.
-Threads pick up tasks from the queue.
-Once a task finishes, the thread becomes available for new tasks.

Types of Thread Pools
-------------------------------------------------------------------------------
Fixed Thread Pool
----------------------------------------------------------------
Fixed number of threads ->Best for consistent task load

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Task implements Runnable {
    private int id;

    public Task(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Task " + id + " executed by " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {e.printStackTrace();}
    }
}

public class FixedThreadPoolExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 6; i++) {
            executor.execute(new Task(i));
        }

        executor.shutdown(); // Prevent new tasks from being accepted
    }
}

// Task 1 executed by pool-1-thread-1
// Task 2 executed by pool-1-thread-2
// Task 3 executed by pool-1-thread-3
// Task 4 executed by pool-1-thread-1
// Task 5 executed by pool-1-thread-2
// Task 6 executed by pool-1-thread-3

Cached Thread Pool
---------------------------------------------------------
Dynamic pool size (based on demand) ->Best for short-lived tasks
New threads are created as needed but are reused if available.
If a thread is idle for 60 seconds, it is terminated.

ExecutorService executor = Executors.newCachedThreadPool();
executor.execute(() -> {
    System.out.println(Thread.currentThread().getName() + " is executing");
});
executor.shutdown();

Single Thread Pool
------------------------------------------------------------------------
Single thread only  ->Ensures task order
Executes one task at a time in a single thread.
Useful for tasks that must execute sequentially in order.

ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(() -> {
    System.out.println(Thread.currentThread().getName() + " is executing");
});
executor.shutdown();

Scheduled Thread Pool
------------------------------------------------------------------------------
Fixed number of threads with delay/scheduling   -> Best for repeated tasks
Used for scheduling tasks with delay or fixed rate

import java.util.concurrent.*;

public class ScheduledExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        scheduler.schedule(() -> 
            System.out.println("Delayed task"), 2, TimeUnit.SECONDS);
        //scheduleAtFixedRate() starts tasks at regular intervals.
        scheduler.scheduleAtFixedRate(() -> 
            System.out.println("Fixed rate task"), 0, 1, TimeUnit.SECONDS);
        //scheduleWithFixedDelay() waits for the previous task to finish.
        scheduler.scheduleWithFixedDelay(() -> 
            System.out.println("Fixed delay task"), 0, 2, TimeUnit.SECONDS);
    }
}


Best Practices for Thread Pools
-----------------------------------------------------------------------
✅ Use a fixed-size pool for consistent workloads.
✅ Use a cached pool for short-lived tasks.
✅ Use a scheduled pool for periodic tasks.
✅ Always call shutdown() to free resources.
✅ Avoid unbounded thread creation → May exhaust system resources.


Fork-Join Framework(introduced in Java 7)
-------------------------------------------------------------------------
Extends AbstractExecutorService.
Uses a work-stealing algorithm — idle threads "steal" work from busy threads. 
Splits the task recursively → Parallel execution + uses Divide-and-Conquer approach

ForkJoinTask<V>     ->Base class for fork-join tasks
RecursiveTask<V>    ->Returns a result
RecursiveAction     ->Does not return a result

✔️ Thread Pools → Best for independent tasks.
✔️ Fork-Join → Best for recursive tasks.

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Arrays;

class BinarySearchTask extends RecursiveTask<Integer> {
    private int[] arr;
    private int target;
    private int start;
    private int end;

    public BinarySearchTask(int[] arr, int target, int start, int end) {
        this.arr = arr;
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (start > end) {
            return -1; // Target not found
        }

        int mid = (start + end) / 2;

        if (arr[mid] == target) {
            return mid; // Target found
        } else if (arr[mid] > target) {
            if (end - start <= 100){ //adjust this number based on array size and desired granularity.
                return sequentialSearch(arr, target, start, mid-1);
            }
            BinarySearchTask left = new BinarySearchTask(arr, target, start, mid - 1);
            return left.invoke();
        } else {
            if (end - start <= 100){
                return sequentialSearch(arr, target, mid+1, end);
            }
            BinarySearchTask right = new BinarySearchTask(arr, target, mid + 1, end);
            return right.invoke();
        }
    }

    private int sequentialSearch(int[] arr, int target, int start, int end){
        for(int i = start; i <= end; i++){
            if(arr[i] == target) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = new int[1000];
        for(int i = 0; i < 1000; i++){
            arr[i] = i * 2;
        }

        int target = 500;
        ForkJoinPool pool = new ForkJoinPool();
        BinarySearchTask task = new BinarySearchTask(arr, target, 0, arr.length - 1);
        int result = pool.invoke(task);

        if (result != -1) {
            System.out.println("Target found at index: " + result);
        } else {
            System.out.println("Target not found.");
        }
    }
}


