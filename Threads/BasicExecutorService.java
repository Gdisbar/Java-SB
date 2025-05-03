/*
/*
---

## ✅ Overview

### `ExecutorService`

`ExecutorService` is an interface in Java (from `java.util.concurrent`) 
that represents an asynchronous execution mechanism for running tasks in the 
background using threads.

---

## 🔁 Key Interfaces and Classes

| Type          | Description                                                                                     |
| ------------- | ----------------------------------------------------------------------------------------------- |
| `Runnable`    | Represents a task that **returns no result** (`void`) and **cannot throw a checked exception**. |
| `Callable<V>` | Represents a task that **returns a result of type V** and **can throw a checked exception**.    |
| `Future<V>`   | Represents the **result of an asynchronous computation**.                                       |

---

## 📌 Relationship Diagram


ExecutorService (interface)
   ↳ ThreadPoolExecutor (class that implements it)
Executors (utility class)
   ↳ has static methods like newFixedThreadPool(int)


---

## 📚 Important Methods of `ExecutorService`

| Method                                          | Description                                                                |
| ----------------------------------------------- | -------------------------------------------------------------------------- |
| `submit(Runnable task)`                         | Returns a `Future<?>`. Task is executed asynchronously.                    |
| `submit(Callable<T> task)`                      | Returns a `Future<T>`. Callable returns a result.                          |
| `invokeAll(Collection<Callable<T>> tasks)`      | Executes all tasks and returns list of Futures. Blocks until all are done. |
| `invokeAny(Collection<Callable<T>> tasks)`      | Returns result of one that completes successfully first.                   |
| `shutdown()`                                    | Initiates orderly shutdown. No new tasks accepted.                         |
| `awaitTermination(long timeout, TimeUnit unit)` | Waits for tasks to finish or timeout.                                      |
| `isShutdown()`                                  | Returns true if shutdown initiated.                                        |
| `isTerminated()`                                | Returns true if all tasks finished after shutdown.                         |

---

## 🎯 `Future<T>` Methods

| Method                                  | Description                                                    |
| --------------------------------------- | -------------------------------------------------------------- |
| `get()`                                 | Waits and returns result of the computation.                   |
| `isDone()`                              | Returns true if task is completed (either success or failure). |
| `isCancelled()`                         | Returns true if task was cancelled.                            |
| `cancel(boolean mayInterruptIfRunning)` | Attempts to cancel execution.                                  |


---

## 📌 Summary

* `ExecutorService` abstracts thread management.
* `FixedThreadPool` helps manage a bounded number of concurrent threads.
* `Runnable` for no-return tasks, `Callable` for tasks with return and exceptions.
* `Future` gives control over submitted task's lifecycle and result.


/*

BASIC EXECUTOR SERVICE EXAMPLE
--------------------------------------------------
1. Submitting a Runnable...
Running task in thread: pool-1-thread-1
Runnable task completed in pool-1-thread-1
Runnable is done: true

2. Submitting a Callable...
Waiting for Callable result...
Calculating result in thread: pool-1-thread-2
Calculation completed in pool-1-thread-2
Result obtained: 42

3. Handling exceptions from a Callable...
About to throw an exception in pool-1-thread-3
Task threw an exception: Deliberate exception for demonstration

4. Executing multiple tasks with invokeAll()...
Result 1: Task 1 completed in pool-1-thread-1
Result 2: Task 2 completed in pool-1-thread-2
Result 3: Task 3 completed in pool-1-thread-3

5. Getting first successful result with invokeAny()...
First completed result: Fastest task finished in pool-1-thread-1

6. Demonstrating task cancellation... after a short delay(1000 ms)
pool-1-thread-1 - Working... 0
pool-1-thread-1 - Working... 1
pool-1-thread-1 - Working... 2
Was task cancelled? true
Is task cancelled? true

7. Shutting down the executor...
Executor is shutdown: true
Executor is terminated: true


*/

import java.util.concurrent.*;
import java.util.*;
import java.io.IOException;
import java.text.SimpleDateFormat;


class BasicExecutorService {
    public static void main(String[] args) {
        System.out.println("BASIC EXECUTOR SERVICE EXAMPLE");
        System.out.println("-".repeat(50));
        
        // Create a fixed thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        try {
            // 1. Submit a Runnable (returns no result)
            System.out.println("1. Submitting a Runnable...");
            Future<?> runnableFuture = executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Running task in thread: " + threadName);
                try {
                    Thread.sleep(1000); // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Runnable task completed in " + threadName);
            });
            
            // Wait for Runnable to complete
            runnableFuture.get(); // This blocks until completion
            System.out.println("Runnable is done: " + runnableFuture.isDone() + "\n");
            
            // 2. Submit a Callable (returns a result)
            System.out.println("2. Submitting a Callable...");
            Future<Integer> callableFuture = executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Calculating result in thread: " + threadName);
                Thread.sleep(1500); // Simulate calculation
                System.out.println("Calculation completed in " + threadName);
                return 42; // Return a result
            });
            
            // Get the result when it's ready
            System.out.println("Waiting for Callable result...");
            Integer result = callableFuture.get(); // This blocks until result is available
            System.out.println("Result obtained: " + result + "\n");
            
            // 3. Exception handling with Callable
            System.out.println("3. Handling exceptions from a Callable...");
            Future<Integer> exceptionFuture = executor.submit(() -> {
                System.out.println("About to throw an exception in " + 
                                   Thread.currentThread().getName());
                Thread.sleep(500);
                throw new IllegalArgumentException("Deliberate exception for demonstration");
            });
            
            try {
                exceptionFuture.get();
            } catch (ExecutionException e) {
                System.out.println("Task threw an exception: " + e.getCause().getMessage() + "\n");
            }
            
            // 4. Execute multiple tasks with invokeAll()
            System.out.println("4. Executing multiple tasks with invokeAll()...");
            List<Callable<String>> tasks = new ArrayList<>();
            tasks.add(() -> {
                Thread.sleep(1000);
                return "Task 1 completed in " + Thread.currentThread().getName();
            });
            tasks.add(() -> {
                Thread.sleep(500);
                return "Task 2 completed in " + Thread.currentThread().getName();
            });
            tasks.add(() -> {
                Thread.sleep(1500);
                return "Task 3 completed in " + Thread.currentThread().getName();
            });
            
            List<Future<String>> results = executor.invokeAll(tasks);
            for (int i = 0; i < results.size(); i++) {
                System.out.println("Result " + (i + 1) + ": " + results.get(i).get());
            }
            System.out.println();
            
            // 5. Get the first successful result with invokeAny()
            System.out.println("5. Getting first successful result with invokeAny()...");
            List<Callable<String>> competingTasks = new ArrayList<>();
            competingTasks.add(() -> {
                Thread.sleep(1000);
                return "Medium-speed task finished in " + Thread.currentThread().getName();
            });
            competingTasks.add(() -> {
                Thread.sleep(500);  // This will finish first
                return "Fastest task finished in " + Thread.currentThread().getName();
            });
            competingTasks.add(() -> {
                Thread.sleep(2000);
                return "Slowest task finished in " + Thread.currentThread().getName();
            });
            
            String firstResult = executor.invokeAny(competingTasks);
            System.out.println("First completed result: " + firstResult + "\n");
            
            // 6. Cancelling a task
            System.out.println("6. Demonstrating task cancellation...");
            Future<String> cancellableFuture = executor.submit(() -> {
                try {
                    String threadName = Thread.currentThread().getName();
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(300);
                        System.out.println(threadName + " - Working... " + i);
                        
                        // Check if interrupted
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println(threadName + " - Task was interrupted");
                            return "Cancelled";
                        }
                    }
                    return "Completed normally";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "Interrupted";
                }
            });
            
            // Cancel the task after a short delay
            Thread.sleep(1000);
            boolean wasCancelled = cancellableFuture.cancel(true); // true means interrupt if running
            System.out.println("Was task cancelled? " + wasCancelled);
            System.out.println("Is task cancelled? " + cancellableFuture.isCancelled() + "\n");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 7. Proper shutdown
            System.out.println("7. Shutting down the executor...");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Not all tasks completed in time, forcing shutdown...");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            System.out.println("Executor is shutdown: " + executor.isShutdown());
            System.out.println("Executor is terminated: " + executor.isTerminated());
        }
    }
}