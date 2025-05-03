
/*

Let's say thread1 gets the pen first.
-> Thread-1 calls acquirePen(), gets the pen's lock.
-> Thread-1 marks the pen as unavailable.
-> Thread-1 calls paper.acquirePaper().
    -> Thread-1 tries to get the paper's lock.
    -> If paper is available, thread-1 gets the paper lock, marks paper as unavailable.
    -> thread-1 calls pen.finishWriting()
    -> thread-1 calls paper.releasePaper()
    -> If paper was not available, thread-1 will wait on paperAvailable condition.

Now let's say thread2 runs.
-> Thread-2 calls acquirePaper(), gets the paper's lock.
-> Thread-2 marks paper as unavailable.
-> Thread-2 calls pen.acquirePen().
    -> Thread-2 tries to get the pen's lock.
    -> If pen is available, thread-2 gets the pen lock, marks pen as unavailable.
    -> thread-2 calls pen.finishWriting()
    -> thread-2 calls paper.releasePaper()
    -> thread-2 calls pen.releasePen()
    -> If pen was not available, thread-2 will wait on penAvailable condition.

*/


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Pen {
    private boolean isAvailable = true;
    private final Lock lock = new ReentrantLock();
    private final Condition penAvailable = lock.newCondition(); // for controlled access

    // locking and waiting if resources are unavailable
    public void acquirePen(Paper paper, String threadName) throws InterruptedException {
        lock.lock();
        try {
            while (!isAvailable) {
                System.out.println(threadName + " is waiting for the pen...");
                penAvailable.await(); // Wait for the pen to become available
            }
            isAvailable = false;
            System.out.println(threadName + " has acquired the pen " + this + " and is trying to use paper " + paper);
            paper.acquirePaper(this, threadName); // Pass the pen to the paper
        } finally {
            lock.unlock();
        }
    }

    // locking and waiting if resources are unavailable
    public void releasePen(String threadName) {
        lock.lock();
        try {
            isAvailable = true;
            System.out.println(threadName + " has released the pen " + this);
            penAvailable.signal(); // Notify threads waiting for the pen
        } finally {
            lock.unlock();
        }
    }

    public void finishWriting(String threadName) {
        System.out.println(threadName + " finished using pen " + this);
    }
}

class Paper {
    private boolean isAvailable = true;
    private final Lock lock = new ReentrantLock();
    private final Condition paperAvailable = lock.newCondition();

    // releasePen and releasePaper release locks and notify waiting threads.
    public void acquirePaper(Pen pen, String threadName) throws InterruptedException {
        lock.lock();
        try {
            while (!isAvailable) {
                System.out.println(threadName + " is waiting for the paper...");
                paperAvailable.await(); // Wait for the paper to become available
            }
            isAvailable = false;
            System.out.println(threadName + " has acquired the paper " + this + " and is trying to use pen " + pen);
            pen.finishWriting(threadName); // Use the pen
            releasePaper(threadName);
        } finally {
            lock.unlock();
        }
    }

    // releasePen and releasePaper release locks and notify waiting threads.
    public void releasePaper(String threadName) {
        lock.lock();
        try {
            isAvailable = true;
            System.out.println(threadName + " has released the paper " + this);
            paperAvailable.signal(); // Notify threads waiting for the paper
        } finally {
            lock.unlock();
        }
    }

    public void finishWriting(String threadName) {
        System.out.println(threadName + " finished using paper " + this);
    }
}

// class ThreadLocks {
//     public static void main(String[] args) throws InterruptedException {
        
//         Pen pen = new Pen();
//         Paper paper = new Paper();
//         Thread thread1 = new Thread(() -> {
//             try {
//                 pen.acquirePen(paper, Thread.currentThread().getName());
//             } catch (InterruptedException e) {
//                 Thread.currentThread().interrupt();
//                 return;
//             }
//             pen.releasePen(Thread.currentThread().getName());
//         }, "Thread-1");
//         Thread thread2 = new Thread(() -> {
//             try {
//                 paper.acquirePaper(pen, Thread.currentThread().getName());
//             } catch (InterruptedException e) {
//                 Thread.currentThread().interrupt();
//                 return;
//             } // releasePaper(threadName) : inside acquirePaper() to avoid dead-lock
//         }, "Thread-2");


//         thread1.start();
//         thread2.start();

   
//         thread1.join();
//         thread2.join();

//         System.out.println("Main thread completed");
//     }
// }

/*


Multiple readers can acquire the read lock without blocking each other, 
but when a thread needs to write, it must acquire the write lock, ensuring 
exclusive access. This prevents data inconsistency while improving read 
efficiency compared to traditional locks, which block all access during 
write operations.

Thread-0 write : 1
Thread-1 read: 1
Thread-2 read: 1
Thread-0 write : 2
Thread-1 read: 2
Thread-2 read: 2
Thread-0 write : 3
Thread-1 read: 3
Thread-2 read: 3
Thread-0 write : 4
Thread-1 read: 4
Thread-2 read: 4
Thread-0 write : 5
Thread-1 read: 5
Thread-2 read: 5
Final count: 5




*/




class ReadWriteCounter {
    private int count = 0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void increment() {
        writeLock.lock();
        try {
            count++;
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    public int getCount() {
        readLock.lock();
        try {
            return count;
        } finally {
            readLock.unlock();
        }
    }
}

class ThreadLocks{
    public static void main(String[] args) throws InterruptedException {
        ReadWriteCounter counter = new ReadWriteCounter();

        Runnable readTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() 
                        + " read: " + counter.getCount());
                }
            }
        };

        Runnable writeTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    counter.increment();
                    System.out.println(Thread.currentThread().getName() 
                        + " write : "+ counter.getCount());
                }
            }
        };

        Thread writerThread = new Thread(writeTask);
        Thread readerThread1 = new Thread(readTask);
        Thread readerThread2 = new Thread(readTask);

        writerThread.start();
        readerThread1.start();
        readerThread2.start();

        writerThread.join();
        readerThread1.join();
        readerThread2.join();

        System.out.println("Final count: " + counter.getCount());
    }
}