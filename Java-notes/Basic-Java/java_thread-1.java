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


