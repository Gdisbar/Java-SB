/*
**Problem Statement:**
Implement a thread-safe counter class that can be incremented and decremented concurrently by multiple threads. Rather than using standard Java synchronization primitives like `synchronized` or `ReentrantLock`, implement your own lock mechanism using atomic operations.

**Requirements:**
1. Create a `CustomLock` class that provides `lock()` and `unlock()` methods
2. Implement a `ThreadSafeCounter` class that uses this custom lock
3. The counter should handle concurrent increments and decrements correctly
4. Include a method to retrieve the current count
5. Demonstrate the counter's thread safety with a multithreaded test

**Expected Solution Approach:**
Use `AtomicBoolean` or another atomic class to implement the lock mechanism. Ensure proper memory visibility between threads using volatile variables or other Java Memory Model compliant approaches.


*/

/*
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class AtomicOps{
    final private AtomicInteger counter = new AtomicInteger(0);
    // false - no lock,operation can be done, true - lock only get
    public volatile boolean lock = false;

//    public  volatile boolean unlock = true;

    protected void increment(){
        if(!lock){ // no lock
            lock = true; // acquire lock
            counter.incrementAndGet();
            System.out.println("incremented : "+getValue());
            lock = false; // release lock
        }else{
            try {
                System.out.println("Can't acquire lock for Increment");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
    protected void decrement(){
        if(!lock){ // no lock
            lock = true;
            counter.decrementAndGet();
            System.out.println("decremented : "+getValue());
            lock = false;
        }else{
            try {
                System.out.println("Can't acquire lock for Decrement");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
    protected int getValue(){
        int result = -1;
        try{
            if(lock){ // true - locked
                result = counter.get();
            }else{
                try {
                    System.out.println("Can't acquire lock for Get");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }finally{
            lock = false; // release lock
        }
        return result;
    }
}

public class ThreadSafeCounter {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AtomicOps atomicOps = new AtomicOps();
        Runnable taskIncrement = () -> {
            for (int i = 0; i < 5; i++) {
                atomicOps.increment();
            }
        };
        Runnable taskDecrement = () -> {
            for (int i = 0; i < 5; i++) {
                atomicOps.decrement();
            }
        };
        Callable<Integer> taskGetvalue = atomicOps::getValue;

        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<?> futureIncrment = executor.submit(() -> taskIncrement);
        Future<Integer> integerFutureAfterIncrement = executor.submit(taskGetvalue);
        System.out.println("After Incement : " +integerFutureAfterIncrement.get());
        Future<?> futureDecrment = executor.submit(() -> taskDecrement);
        Future<Integer> integerFutureAfterDecrement = executor.submit(taskGetvalue);
        System.out.println("After Decement : " +integerFutureAfterDecrement.get());

    }
}

 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class customLock{
    protected AtomicBoolean locked = new AtomicBoolean(false);
    protected void lock(){
        while (locked.compareAndSet(false,true)){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected void unlock(){
        locked.set(false);
    }
}

class AtomicOps{
    protected AtomicInteger counter = new AtomicInteger(0);
    protected customLock lock = new customLock();
    protected void increment(){
        try {
            lock.lock();
            counter.incrementAndGet();
            System.out.println("incremented : "+getValue());
        }finally {
            lock.unlock();
        }
    }
    protected void decrement(){
        try {
            lock.lock();
            counter.decrementAndGet();
            System.out.println("decremented : "+getValue());
        }finally {
            lock.unlock();
        }
    }
    protected int getValue(){
        return counter.get();
    }
}
public class ThreadSafeCounter {
    public static void main(String[] args) throws InterruptedException {
        AtomicOps atomicOps = new AtomicOps();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(()->{
            for (int i = 0; i < 10; i++) {
                atomicOps.increment();
            }
        });
        executor.submit(()->{
            for (int i = 0; i < 5; i++) {
                atomicOps.decrement();
            }
        });
        executor.shutdown();
        executor.awaitTermination(40, TimeUnit.SECONDS);
        System.out.println("Final Count: " + atomicOps.getValue());
    }
}
