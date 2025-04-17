/**
 * Create a basic implementation of a read-write lock that allows 
 * multiple readers but only one writer at a time. Include proper exception 
 * handling for lock acquisition and release.


 * 
 * **/

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReadWrite {
    private int readers = 0;
    private boolean writing = false;
    private final Lock lock = new ReentrantLock();
    private final Condition canRead = lock.newCondition();
    private final Condition canWrite = lock.newCondition();

    public void readLock() throws InterruptedException {
        lock.lock();
        try {
            while (writing) {
                canRead.await();
            }
            readers++;
        } finally {
            lock.unlock();
        }
    }

    public void readUnlock() {
        lock.lock();
        try {
            readers--;
            if (readers == 0) {
                canWrite.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void writeLock() throws InterruptedException {
        lock.lock();
        try {
            while (writing || readers > 0) {
                canWrite.await();
            }
            writing = true;
        } finally {
            lock.unlock();
        }
    }

    public void writeUnlock() {
        lock.lock();
        try {
            writing = false;
            canWrite.signal();
            canRead.signalAll();
        } finally {
            lock.unlock();
        }
    }
    // Helper for reader task
    public Runnable readerTask(String name, long sleepMillis) {
        return () -> {
            try {
                readLock();
                System.out.println(name + " reading...");
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(name + " interrupted during read");
            } finally {
                readUnlock();
                System.out.println(name + " read unlock");
            }
        };
    }

    // Helper for writer task
    public Runnable writerTask(String name, long sleepMillis) {
        return () -> {
            try {
                writeLock();
                System.out.println(name + " writing...");
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(name + " interrupted during write");
            } finally {
                writeUnlock();
                System.out.println(name + " write unlock");
            }
        };
    }
}

public class ReadWriteLock {
    public static void main(String[] args) {
        ReadWrite lock = new ReadWrite();

        Thread[] threads = {
            new Thread(lock.readerTask("Reader 1", 100)),
            new Thread(lock.readerTask("Reader 2", 100)),
            new Thread(lock.writerTask("Writer 1", 200)),
            new Thread(lock.readerTask("Reader 3", 100)),
            new Thread(lock.writerTask("Writer 2", 200))
        };

        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Main thread interrupted");
            }
        }

        System.out.println("Main thread finished");
    }
}

/*

Feature | Description
✅ Java 8 Lambdas | Used instead of anonymous Runnable classes.
✅ Cleaner code | No need for separate ReaderThread and WriterThread classes.
✅ Reusable task creators | readerTask() and writerTask() methods make the code extensible.
✅ Named threads | Names passed in help trace behavior in logs.

*/


// class ReaderThread implements Runnable {
//         private ReadWrite lock;

//         public ReaderThread(ReadWrite lock) {
//             this.lock = lock;
//         }

//         @Override
//         public void run() {
//             try {
//                 lock.readLock();
//                 System.out.println(Thread.currentThread().getName() + " reading");
//                 Thread.sleep(100);
//             } catch (InterruptedException e) {
//                 Thread.currentThread().interrupt();
//                 System.err.println(Thread.currentThread().getName() + " interrupted during read");
//             } finally {
//                 lock.readUnlock();
//                 System.out.println(Thread.currentThread().getName() + " read unlock");
//             }
//         }
//     }



// public class ReadWriteLock {
//     public static void main(String[] args) {
//         ReadWrite lock = new ReadWrite();
//         ReaderThread reader = new ReaderThread(lock);
//         Thread t1 = new Thread(reader, "Reader 1");
//         Thread t2 = new Thread(reader, "Reader 2");
//         Thread t3 = new Thread(new WriterThread(lock), "Writer 1");
//         Thread t4 = new Thread(reader, "Reader 3");
//         Thread t5 = new Thread(new WriterThread(lock), "Writer 2");

//         t1.start();
//         t2.start();
//         t3.start();
//         t4.start();
//         t5.start();

//         try {
//             t1.join();
//             t2.join();
//             t3.join();
//             t4.join();
//             t5.join();
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//             System.err.println("Main thread interrupted");
//         }

//         System.out.println("Main thread finished");
//     }

//     static class WriterThread implements Runnable {
//         private ReadWrite lock;

//         public WriterThread(ReadWrite lock) {
//             this.lock = lock;
//         }

//         @Override
//         public void run() {
//             try {
//                 lock.writeLock();
//                 System.out.println(Thread.currentThread().getName() + " writing");
//                 Thread.sleep(200);
//             } catch (InterruptedException e) {
//                 Thread.currentThread().interrupt();
//                 System.err.println(Thread.currentThread().getName() + " interrupted during write");
//             } finally {
//                 lock.writeUnlock();
//                 System.out.println(Thread.currentThread().getName() + " write unlock");
//             }
//         }
//     }  
// }

/*
Since here we're using ReaderThread(lock) & WriterThread(lock) multiple times - It makes
sense to implemet them rather than using lambda function
*/