
/*
### ✅ Compact Table of `ReentrantLock` Methods Usage

| **Method**               | **Used In**                   | **Behavior**                                                                  |
| ------------------------ | ----------------------------- | ----------------------------------------------------------------------------- |
| `lock()`                 | `withdrawWithLock`            | Blocks until lock is acquired. May wait indefinitely.                         |
| `tryLock()`              | `withdrawWithTryLock`         | Non-blocking. Acquires lock if available; else returns `false`.               |
| `tryLock(timeout, unit)` | `withdrawWithTimeout`         | Waits up to timeout to acquire lock. Returns `false` if not acquired in time. |
| `lockInterruptibly()`    | `withdrawWithInterruptible`   | Blocks unless interrupted while waiting.                                      |
| `unlock()`               | `finally` block (all methods) | Releases the lock, must be in `finally` to avoid deadlock.                    |


private final Lock lock = new ReentrantLock(true);

However, fairness can lead to lower throughput due to the overhead of maintaining the order.
Non-fair locks, in contrast, allow threads to “cut in line,” potentially offering 
better performance but at the risk of some threads waiting indefinitely if others 
frequently acquire the lock.

---

### 🔁 Locking Sequence 


Thread-1 (withdrawWithLock)
  ---> Calls lock()
        ---> Waits (if needed)
              ---> Acquires lock
                    ---> Withdraws $300
                          ---> Releases lock (finally)

Thread-2 (withdrawWithTryLock)
  ---> Calls tryLock()
        ---> If lock acquired?
              ---> Yes ---> Withdraws $300 ---> Releases lock (finally)
              ---> No  ---> Skips withdrawal (lock not available)

Thread-3 (withdrawWithTimeout)
  ---> Calls tryLock(2s)
        ---> Waits up to 2 seconds
              ---> If lock acquired in time?
                    ---> Yes ---> Withdraws $300 ---> Releases lock (finally)
                    ---> No  ---> Skips withdrawal (timeout)

Thread-4 (withdrawWithInterruptible)
  ---> Calls lockInterruptibly()
        ---> If interrupted?
              ---> Yes ---> Exit with InterruptedException
              ---> No  ---> Acquires lock
                            ---> Withdraws $300
                                  ---> Releases lock (finally)

*/
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private int balance = 1000;
    // true - Fairness lock
    private final ReentrantLock lock = new ReentrantLock();

    public void withdrawWithLock(String threadName, int amount) {
        lock.lock();
        try {
            System.out.println(threadName + " acquired lock using lock()");
            if (balance >= amount) {
                System.out.println(threadName + " is withdrawing " + amount);
                balance -= amount;
                System.out.println(threadName + " completed withdrawal. Remaining balance: " + balance);
            } else {
                System.out.println(threadName + " insufficient balance.");
            }
        } finally {
            lock.unlock();
            System.out.println(threadName + " released lock");
        }
    }

    public void withdrawWithTryLock(String threadName, int amount) {
        if (lock.tryLock()) {
            try {
                System.out.println(threadName + " acquired lock using tryLock()");
                if (balance >= amount) {
                    System.out.println(threadName + " is withdrawing " + amount);
                    balance -= amount;
                    System.out.println(threadName + " completed withdrawal. Remaining balance: " + balance);
                } else {
                    System.out.println(threadName + " insufficient balance.");
                }
            } finally {
                lock.unlock();
                System.out.println(threadName + " released lock");
            }
        } else {
            System.out.println(threadName + " couldn't acquire lock using tryLock()");
        }
    }

    public void withdrawWithTimeout(String threadName, int amount) {
        try {
            if (lock.tryLock(2, TimeUnit.SECONDS)) {
                try {
                    System.out.println(threadName + " acquired lock using tryLock(timeout)");
                    if (balance >= amount) {
                        System.out.println(threadName + " is withdrawing " + amount);
                        balance -= amount;
                        System.out.println(threadName + " completed withdrawal. Remaining balance: " + balance);
                    } else {
                        System.out.println(threadName + " insufficient balance.");
                    }
                } finally {
                    lock.unlock();
                    System.out.println(threadName + " released lock");
                }
            } else {
                System.out.println(threadName + " timed out trying to acquire lock");
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " was interrupted while trying to acquire lock");
        }
    }

    public void withdrawWithInterruptible(String threadName, int amount) {
        try {
            lock.lockInterruptibly();
            try {
                System.out.println(threadName + " acquired lock using lockInterruptibly()");
                if (balance >= amount) {
                    System.out.println(threadName + " is withdrawing " + amount);
                    balance -= amount;
                    System.out.println(threadName + " completed withdrawal. Remaining balance: " + balance);
                } else {
                    System.out.println(threadName + " insufficient balance.");
                }
            } finally {
                lock.unlock();
                System.out.println(threadName + " released lock");
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " was interrupted while acquiring lockInterruptibly()");
        }
    }

    
}


class ReentrantLockBanking{
	public static void main(String[] args) {
        BankAccount account = new BankAccount();

        Thread t1 = new Thread(() -> account.withdrawWithLock("Thread-1", 300));
        Thread t2 = new Thread(() -> account.withdrawWithTryLock("Thread-2", 300));
        Thread t3 = new Thread(() -> account.withdrawWithTimeout("Thread-3", 300));
        Thread t4 = new Thread(() -> account.withdrawWithInterruptible("Thread-4", 300));

        t1.start();
        t2.start();
        t3.start();

        // Interrupt thread-4 after a short delay
        t4.start();
        try {
            Thread.sleep(500);
            t4.interrupt(); // May cause it to skip or break from lockInterruptibly()
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
