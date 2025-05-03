/*

Thread Synchronization
--------------------------------------------------------------------
When multiple threads access shared data simultaneously → Race conditions occur.
Synchronization ensures that only one thread accesses the critical section at a time.

- Synchronized Method (Only one thread can access a synchronized method at a time.)
- Synchronized Block (Allows finer control over the lock)

2. Livelocks
   - Similar to a deadlock but threads continuously change state in response to each 
   other without making progress.
   - Example: Two threads repeatedly try to acquire the same locks and release them 
   upon detecting contention.

3. Thread Starvation  
   - Occurs when low-priority threads never get CPU time because high-priority threads 
   keep executing.
   - Solution: Fair scheduling policies or priority adjustments.


*/

// Thread Communication
/* ========================================================================= */

class SharedResource {
    private int data;
    private boolean hasData;

    public synchronized void produce(int value) {
        while (hasData) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        data = value;
        hasData = true;
        System.out.println("Produced: " + value);
        notify();
    }

    public synchronized int consume() {
        while (!hasData){
            try{
                wait();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        hasData = false;
        System.out.println("Consumed: " + data);
        notify();
        return data;
    }
}

public class ThreadCommunication {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();
        Thread producerThread = new Thread(()->{
                for (int i = 0; i < 10; i++) {
                    resource.produce(i);
                }
        });
        Thread consumerThread = new Thread(()->{
                for (int i = 0; i < 10; i++) {
                    int value = resource.consume();
                }
        });

        producerThread.start();
        consumerThread.start();
    }
}

// This prevents deadlock and ensures orderly resource sharing.
/* ============================================================================= */

class Pen {
    public synchronized void writeWithPenAndPaper(Paper paper) {
        System.out.println(Thread.currentThread().getName() + 
            " is using pen " + this + " and trying to use paper " + paper);
        paper.finishWriting();
    }

    public synchronized void finishWriting() {
        System.out.println(Thread.currentThread().getName() + 
            " finished using pen " + this);
    }
}

class Paper {
    public synchronized void writeWithPaperAndPen(Pen pen) {
        System.out.println(Thread.currentThread().getName() + 
            " is using paper " + this + " and trying to use pen " + pen);
        pen.finishWriting();
    }

    public synchronized void finishWriting() {
        System.out.println(Thread.currentThread().getName() + 
            " finished using paper " + this);
    }
}


public class ThreadCommunication {
    public static void main(String[] args) {
        Pen pen = new Pen();
        Paper paper = new Paper();

        Thread thread1 = new Thread(()->{
            pen.writeWithPenAndPaper(paper); // thread1 locks pen and tries to lock paper
        }, "Thread-1");
        Thread thread2 = new Thread(()->{
            synchronized (pen){
                paper.writeWithPaperAndPen(pen); // thread2 locks paper and tries to lock pen
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();
    }
}

