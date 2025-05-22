package Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskSubmissionSystem {
    private static ConcurrentLinkedQueue<String> taskQueue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();

        int numberOfProducers = 5;
        int numberOfConsumers = 5;
        int tasksPerProducer = 2; // Each producer adds 2 tasks
        int tasksToProcessPerConsumer = 2; // Each consumer tries to process 2 tasks
        // Note: The total number of tasks produced will be numberOfProducers * tasksPerProducer
        // You might need to adjust tasksToProcessPerConsumer if you want consumers to consume all tasks.

        // Create and start multiple producer threads
        for (int i = 0; i < numberOfProducers; i++) {
            Thread producer = new Thread(() -> {
                int producerCounter = tasksPerProducer;
                while (producerCounter > 0) {
                    try {
                        String task = "Task " + System.currentTimeMillis() + " from Producer " + Thread.currentThread().getId();
                        taskQueue.add(task);
                        System.out.println("Produced: " + task);
                        producerCounter -= 1;
                        Thread.sleep(50); // Small delay to simulate work and allow other threads to run
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        System.err.println("Producer interrupted: " + e.getMessage());
                        break; // Exit the loop if interrupted
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            producers.add(producer); // Add to list to join later
            producer.start();
        }

        // Create and start multiple consumer threads
        for (int i = 0; i < numberOfConsumers; i++) {
            Thread consumer = new Thread(() -> {
                int consumerCounter = tasksToProcessPerConsumer;
                // A more robust consumer might poll until a "poison pill" is received or a shared counter reaches zero
                while (consumerCounter > 0 || !taskQueue.isEmpty()) { // Continue as long as tasks need processing OR queue is not empty
                    try {
                        String task = taskQueue.poll();
                        if (task != null) {
                            System.out.println("Processing: " + task + " by Consumer " + Thread.currentThread().getId());
                            consumerCounter -= 1;
                            Thread.sleep(100); // Small delay to simulate work
                        } else {
                            // If the queue is empty, consumer waits a bit before checking again
                            // This is a busy-wait, for production, consider wait/notify or BlockingQueue
                            if (consumerCounter > 0) { // Only print waiting message if still expecting tasks
                                System.out.println("Consumer " + Thread.currentThread().getId() + " waiting for tasks...");
                            }
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        System.err.println("Consumer interrupted: " + e.getMessage());
                        break; // Exit the loop if interrupted
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consumer " + Thread.currentThread().getId() + " finished processing tasks.");
            });
            consumers.add(consumer); // Add to list to join later
            consumer.start();
        }

        // Wait for all producer threads to complete
        System.out.println("\nWaiting for all producers to finish...");
        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Main thread interrupted while waiting for producer: " + e.getMessage());
            }
        }
        System.out.println("All producers finished.");

        // Wait for all consumer threads to complete
        // Important: You need a way to signal consumers that no more tasks will be produced.
        // Without it, consumers might busy-wait indefinitely if their `consumerCounter` isn't reached
        // or if the queue becomes empty. For this example, they will eventually exit if their counter
        // reaches zero OR if the queue is empty AND they have fulfilled their `tasksToProcessPerConsumer`.
        System.out.println("Waiting for all consumers to finish...");
        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Main thread interrupted while waiting for consumer: " + e.getMessage());
            }
        }
        System.out.println("All consumers finished.");

        System.out.println("Program execution complete.");
    }
}