/*
Basic creation	- supplyAsync, runAsync
Transformations -	thenApply, thenCompose
Combining futures -	thenCombine
Consuming result	- thenAccept
Error handling	- exceptionally, handle
Stream Operation - allOf, anyOf

Recovered from error: java.lang.RuntimeException: Oops!
Handled: java.lang.RuntimeException: Oops!
First to finish: Fast
Running background task
Combined Result: CompletableFuture & CompletableFuture Transformations
All tasks done

*/

import java.util.concurrent.*;
import java.util.*;

public class CompletableFutureOps {

	private static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // --- 1. Basic creation ---
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return "CompletableFuture";
        }, executor);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            sleep(300);
            System.out.println("Running background task");
        }, executor);

        // --- 2. Transformations ---
        CompletableFuture<String> future3 = future1.thenApply(result -> result + " Transformations");
        CompletableFuture<Integer> future4 = future1.thenCompose(result -> 
            CompletableFuture.supplyAsync(() -> result.length(), executor)
        );

        // --- 3. Combining futures ---
        CompletableFuture<String> combined = future1.thenCombine(future3, 
            (r1, r2) -> r1 + " & " + r2);

        // --- 4. Consuming result ---
        combined.thenAccept(result -> System.out.println("Combined Result: " + result));

        // --- 5. Error handling ---
        CompletableFuture<String> errorFuture = CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("Oops!");
            return "Won't reach here";
        });

        errorFuture
            .exceptionally(ex -> "Recovered from error: " + ex.getMessage())
            .thenAccept(System.out::println);

        // handle gives both result and exception
        errorFuture
            .handle((result, ex) -> {
                if (ex != null) return "Handled: " + ex.getMessage();
                return result;
            }).thenAccept(System.out::println);

        // --- 6. allOf / anyOf ---
        CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> {
            sleep(200); return "Fast";
        });

        CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
            sleep(1000); return "Slow";
        });

        CompletableFuture<Void> all = CompletableFuture.allOf(fast, slow);
        all.thenRun(() -> System.out.println("All tasks done"));

        CompletableFuture<Object> any = CompletableFuture.anyOf(fast, slow);
        any.thenAccept(first -> System.out.println("First to finish: " + first));

        // Wait for all tasks to finish before shutting down
        all.join(); 
        executor.shutdown();
    }
}

    

    

