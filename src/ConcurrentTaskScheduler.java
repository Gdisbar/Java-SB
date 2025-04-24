import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;


class TaskScheduler {
    private ExecutorService executor;
    protected TaskScheduler(ExecutorService executor){
        this.executor = executor;
    }
    public <T> CompletableFuture<List<T>> scheduleAll(List<Supplier<T>> tasks) {
        // Execute all tasks concurrently and collect results in original order
        // 1. Submit each task and convert to CompletableFuture, maintaining order
        List<CompletableFuture<T>> futures = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, executor))
                .collect(Collectors.toList());

        // 2. Submit each task asynchronously using the thread pool.
        // CompletableFuture.allOf() - Waits for all futures to complete.
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // 3. After all complete, collect the results in the original order
        // thenAccept(), thenApply() - Functional composition after future completes.
        return allDoneFuture.thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    public <T> CompletableFuture<T> scheduleFirstCompleted(List<Supplier<T>> tasks) {
        // Return the result of the first task to complete successfully
        List<CompletableFuture<T>> futures = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, executor))
                .collect(Collectors.toList());
        // CompletableFuture.anyOf() - Completes when any one future completes.
        CompletableFuture<?> anyOfFuture = CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0]));
        // Cast the result to the expected type T. It's important to handle potential
        // exceptions or cases where all tasks might complete exceptionally.
        return anyOfFuture.thenApply(result -> (T) result);
    }

    public void shutdown() {
        executor.shutdown();
    }
}

public class ConcurrentTaskScheduler {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TaskScheduler scheduler = new TaskScheduler(Executors.newFixedThreadPool(3));
        List<Supplier<String>> stringTask = Arrays.asList(
                ()->{
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    return "stringTask --> Thread 1 running";
                },
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    return "stringTask --> Thread 2 running";
                },
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    return "stringTask --> Thread 3 running";
                }
        );
        // CompletableFuture - Used for running async tasks and composing their results.
        CompletableFuture<List<String>> allResultsFuture= scheduler.scheduleAll(stringTask);
        try {
            System.out.println("\n--- Testing scheduleAll ---");
            // thenAccept(), thenApply() - Functional composition after future completes.
            allResultsFuture.thenAccept(result->{
                result.forEach(System.out::println);
            }).get();// block until not complete
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        List<Supplier<Integer>> integerTasks = Arrays.asList(
                ()->{
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    return 1;
                },
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    return 2;
                },
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(2000);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    return 3;
                }
        );
        System.out.println("\n--- Testing scheduleFirstCompleted ---");
        CompletableFuture<Integer> firstResultFuture = scheduler.scheduleFirstCompleted(integerTasks);
        try {
            // thenAccept(), thenApply() - Functional composition after future completes.
            firstResultFuture.thenAccept(result -> {
                System.out.println("First completed task result: " + result);
            }).get(); // Block and wait for completion
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        scheduler.shutdown(); // Shut down the executor


    }
}



