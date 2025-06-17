/*

## Problem 5: Asynchronous Task Executor with CompletableFuture

**Problem Statement:**
Design an asynchronous task execution framework that processes tasks in parallel with dependencies between them. Use modern Java concurrency features like `CompletableFuture`.

**Requirements:**
1. Create a `Task` interface with an `execute()` method returning a `CompletableFuture`
2. Implement a `TaskExecutor` class that can schedule and run tasks
3. Support task dependencies - a task can depend on completion of other tasks
4. Include timeout handling and error propagation
5. Implement a mechanism to limit the number of concurrently executing tasks
6. Provide a way to cancel all in-progress tasks

**Expected Solution Approach:**
Use CompletableFuture for asynchronous execution and composition, along with an ExecutorService for thread management.



* */

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


interface Task {
    CompletableFuture<Object> execute();
}

class SimpleTask implements Task {
    private final String name;

    public SimpleTask(String name) {
        this.name = name;
    }

    @Override
    public CompletableFuture<Object> execute() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Running task: " + name);
            try {
                Thread.sleep(1000); // Simulate work
            } catch (InterruptedException e) {
                throw new RuntimeException("Task interrupted");
            }
            return "Result - " + name;
        });
    }
}

class DependentTask implements Task {
    private List<Object> dependencyResults;
    private final String name;
    private ExecutorService executor;

    public DependentTask(String name) {
        this.name = name;
    }

    public void setDependencies(List<Object> results, ExecutorService executor) {
        this.dependencyResults = results;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Object> execute() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Running dependent task: " + name + " with dependencies: " + dependencyResults);
            return "Dependent result of " + name;
        }, executor);
    }
}

class TaskExecutor {
    private final Map<String, CompletableFuture<Object>> activeTasks = new ConcurrentHashMap<>();
    private final Map<String, List<String>> taskGraph = new ConcurrentHashMap<>();
    private final ExecutorService executor;
    private final long timeoutMillis;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public TaskExecutor(int concurrencyLimit, long timeoutMillis) {
        this.executor = Executors.newFixedThreadPool(concurrencyLimit);
        this.timeoutMillis = timeoutMillis;
    }

    public CompletableFuture<Object> submitTask(String taskId, Task task, List<String> dependencies) {
        if (cancelled.get()) return CompletableFuture.failedFuture(new CancellationException("Executor cancelled"));
        if (activeTasks.containsKey(taskId)) return CompletableFuture.failedFuture(new IllegalArgumentException("Task already exists"));

        List<CompletableFuture<Object>> dependencyFutures = new ArrayList<>();
        for (String depId : dependencies) {
            CompletableFuture<Object> depFuture = activeTasks.get(depId);
            if (depFuture == null) {
                return CompletableFuture.failedFuture(new IllegalArgumentException("Missing dependency: " + depId));
            }
            dependencyFutures.add(depFuture);
        }

        CompletableFuture<Object> finalFuture;
        if (dependencyFutures.isEmpty()) {
            finalFuture = task.execute().orTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
        } else {
            finalFuture = CompletableFuture.allOf(dependencyFutures.toArray(new CompletableFuture[0]))
                    .thenComposeAsync(v -> {
                        List<Object> results = dependencyFutures.stream().map(CompletableFuture::join).toList();
                        if (task instanceof DependentTask dt) {
                            dt.setDependencies(results, executor);
                        }
                        return task.execute().orTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
                    }, executor);
        }

        finalFuture.whenComplete((res, ex) -> {
            if (ex != null) System.err.println("Task " + taskId + " failed: " + ex.getMessage());
            else System.out.println("Task " + taskId + " completed: " + res);
            activeTasks.remove(taskId);
        });

        activeTasks.put(taskId, finalFuture);
        taskGraph.put(taskId, dependencies);
        return finalFuture;
    }

    public void cancelAll() {
        cancelled.set(true);
        activeTasks.values().forEach(future -> future.cancel(true));
        executor.shutdownNow();
    }
}

public class AsyncTaskExecutor {
    public static void main(String[] args) throws InterruptedException {
        TaskExecutor executor = new TaskExecutor(4, 3000);

        executor.submitTask("task1", new SimpleTask("task1"), List.of());
        executor.submitTask("task2", new SimpleTask("task2"), List.of());

        Thread.sleep(100); // Ensure task1 and task2 are registered

        executor.submitTask("task3", new DependentTask("task3"), List.of("task1", "task2"));

        Thread.sleep(500); // Let tasks complete - increase
        executor.cancelAll();
    }
}
