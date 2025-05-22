package Queue;

//All Known Implementing Classes:
//---------------------------------------------------------------------------
//ArrayBlockingQueue, DelayQueue, LinkedBlockingDeque, LinkedBlockingQueue,
//LinkedTransferQueue, PriorityBlockingQueue, SynchronousQueue

//Insert methods
//=============================================
//add(e) : Throws exception
//offer(e) : Either null or false, depending on the operation
//put(e) : Blocks
//offer(e, time, unit) : Times out
//
//Remove
//==========================================
//remove() : Throws exception
//poll() : Either null or false, depending on the operation
//take() : Blocks
//poll(time, unit) : Times out
//
//Examine
//===============================
//element() : Throws exception
//peek() : Either null or false, depending on the operation

//A BlockingQueue may be capacity bounded. At any given time it may have a remainingCapacity
//beyond which no additional elements can be put without blocking. A BlockingQueue without any
//intrinsic capacity constraints always reports a remaining capacity of Integer.MAX_VALUE.

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueBasic {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new SynchronousQueue<>();
        CountDownLatch firstConsumerDone = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            try {
                System.out.println("Producer is waiting to transfer...");
                queue.put("Hello from producer!");
                queue.put("Hello from producer!");
                System.out.println("Producer has transferred both messages.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer was interrupted.");
            }
        });

        Thread consumerPrevious = new Thread(() -> {
            try {
                System.out.println("Previous Consumer is waiting to receive...");
                String message = queue.take();
                System.out.println("Previous Consumer received: " + message);
                firstConsumerDone.countDown(); // Notify that previous consumer is done
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Previous Consumer was interrupted.");
            }
        });

        Thread consumerCurrent = new Thread(() -> {
            try {
                firstConsumerDone.await(); // Wait for previous consumer to finish
                System.out.println("Current Consumer is waiting to receive...");
                String message = queue.take();
                System.out.println("Current Consumer received: " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Current Consumer was interrupted.");
            }
        });

        producer.start();
        consumerPrevious.start();
        consumerCurrent.start();
    }
}
