/*

SCHEDULED TASKS EXAMPLE
--------------------------------------------------

⏳ Scheduler Execution Flow (Timeline: from 19:45:42 to \~19:45:52)


19:45:42.691 ---> [Start of Program] ---> Scheduler created with 2 threads
               ---> Delayed task scheduled (executes after 3 sec)
               ---> Fixed-rate task scheduled (starts after 1 sec, repeats every 1 sec)
               ---> Fixed-delay task scheduled (starts immediately, 2 sec delay after each completion)

19:45:42.710 ---> Fixed-delay task starts (Thread-1)
19:45:43.355 ---> Fixed-delay task ends
               ---> Fixed-rate task executes (Thread-2)

19:45:44.709 ---> Fixed-rate task executes (Thread-2)
19:45:45.356 ---> Fixed-delay task starts (Thread-1)
19:45:45.707 ---> Delayed task executes (Thread-2) [after 3 sec from scheduling]
19:45:45.710 ---> Fixed-rate task executes (Thread-2)

19:45:46.314 ---> Fixed-delay task ends
19:45:46.709 ---> Fixed-rate task executes (Thread-2)

19:45:47.709 ---> Fixed-rate task executes (Thread-2)
19:45:48.315 ---> Fixed-delay task starts (Thread-1)
19:45:48.689 ---> Fixed-delay task ends
19:45:48.709 ---> Fixed-rate task executes (Thread-2)

19:45:49.709 ---> Fixed-rate task executes (Thread-2)
19:45:50.690 ---> Fixed-delay task starts (Thread-1)
19:45:50.709 ---> Fixed-rate task executes (Thread-2)
19:45:51.241 ---> Fixed-delay task ends
19:45:51.709 ---> Fixed-rate task executes (Thread-2)

19:45:52.709 ---> Fixed-rate task executes (Thread-2)

[AFTER 10 SECONDS]
               ---> Cancelling fixed-rate task
               ---> Cancelling fixed-delay task
               ---> Confirm delayed task is done

               ---> Shutdown scheduler


### 🔁 Execution Rules Recap

* Delayed Task: Runs once after 3 seconds → Executes around 19:45:45.707
* Fixed-Rate Task: Executes every 1 second, regardless of previous task completion time
* Fixed-Delay Task: Starts after initial delay = 0 sec, and then starts 2 sec 
        after previous task ends



*/

import java.util.concurrent.*;
import java.util.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

class ScheduledExecutor {
    public static void main(String[] args) {
        System.out.println("SCHEDULED TASKS EXAMPLE");
        System.out.println("-".repeat(50));
        
        // Create a scheduled executor with 2 threads
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
            
            System.out.println("Current time: " + timeFormat.format(new Date()));
            
            // 1. Schedule a one-time task to run after a delay
            System.out.println("1. Scheduling a delayed one-time task (3 seconds)...");
            ScheduledFuture<?> delayedTask = scheduler.schedule(() -> {
                System.out.println(timeFormat.format(new Date()) + 
                                  " - Delayed task executed in " + 
                                  Thread.currentThread().getName());
            }, 3, TimeUnit.SECONDS);
            
            // 2. Schedule a task to run repeatedly with a fixed rate
            System.out.println("2. Scheduling a fixed-rate repeated task (every 1 second)...");
            ScheduledFuture<?> fixedRateTask = scheduler.scheduleAtFixedRate(() -> {
                System.out.println(timeFormat.format(new Date()) + 
                                  " - Fixed rate task executed in " + 
                                  Thread.currentThread().getName());
            }, 1, 1, TimeUnit.SECONDS);
            
            // 3. Schedule a task with fixed delay between task completions
            System.out.println("3. Scheduling a fixed-delay repeated task (2 second delay after completion)...");
            ScheduledFuture<?> fixedDelayTask = scheduler.scheduleWithFixedDelay(() -> {
                System.out.println(timeFormat.format(new Date()) + 
                                  " - Fixed delay task starting in " + 
                                  Thread.currentThread().getName());
                try {
                    // This task takes variable time to complete
                    Thread.sleep((long)(Math.random() * 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(timeFormat.format(new Date()) + 
                                  " - Fixed delay task completed in " + 
                                  Thread.currentThread().getName());
            }, 0, 2, TimeUnit.SECONDS);
            
            // Let the tasks run for a while
            System.out.println("\nLetting scheduled tasks run for 10 seconds...");
            Thread.sleep(10000);
            
            // Cancel the recurring tasks
            System.out.println("\nCancelling recurring tasks...");
            fixedRateTask.cancel(false);
            fixedDelayTask.cancel(false);
            
            System.out.println("Fixed rate task cancelled: " + fixedRateTask.isCancelled());
            System.out.println("Fixed delay task cancelled: " + fixedDelayTask.isCancelled());
            
            // Wait for delayed task to complete (if it hasn't already)
            try {
                delayedTask.get();
                System.out.println("Delayed task completed: " + delayedTask.isDone());
            } catch (Exception e) {
                System.out.println("Error with delayed task: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("\nShutting down the scheduler...");
            scheduler.shutdown();
        }
    }
}
