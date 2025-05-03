/*
Process (Running two instances of a browser)
-----------------------------------------------------
Process is a program in execution.Each process has its own memory space
Processes communicate using IPC (Inter-Process Communication)

Thread (Opening two tabs in a browser)
---------------------------------------------------------
A lightweight subtask within a process, Threads within a process share memory
Threads communicate using shared memory

Java provides robust support for multithreading through the 
java.lang.Thread class, Runnable interface, Executor Framework, and various concurrent utilities.

// Start a new process using ProcessBuilder
try {
    Process process = new ProcessBuilder("notepad.exe").start();
    System.out.println("Process started");
} catch (Exception e) {
    e.printStackTrace();
}

Single-threaded vs Multithreaded Execution
-----------------------------------------------------------------------
Single-threaded -> One task is executed at a time
Multithreaded   -> Multiple tasks are executed concurrently
Parallelism     -> True simultaneous execution (multiple processors)
Concurrency     -> Tasks are managed in an overlapping time frame

Why Multithreading is Needed?
✔️ Efficient CPU utilization
✔️ Improved performance
✔️ Better resource sharing
✔️ Faster response times
✔️ Handling large I/O-bound operations




Best Practices
----------------------------------
✅ Minimize the scope of synchronized blocks.
✅ Avoid holding locks for too long.
✅ Use ExecutorService over manual thread creation.
✅ Handle InterruptedException properly.
✅ Use ConcurrentHashMap instead of Hashtable.
✅ Use lock ordering to prevent deadlocks
✅ Release locks in finally blocks
✅ Consider higher-level concurrency utilities when appropriate
✅ Use thread-safe collections from java.util.concurrent package
✅ Prefer atomic variables for simple counters and flags
✅ Avoid nested locks when possible

Thread Lifecycle
=============================================================================================
The lifecycle of a thread in Java consists of several states, which a thread can move
through during its execution.

New: A thread is in this state when it is created but not yet started.

Runnable: After the start method is called, the thread becomes runnable. It’s ready to
run and is waiting for CPU time.

Running: The thread is in this state when it is executing.

Blocked/Waiting: A thread is in this state when it is waiting for a resource or for
another thread to perform an action.

Terminated: A thread is in this state when it has finished executing.

join( ): Waits for this thread to die. When one thread calls the join() method of
another thread, it pauses the execution of the current thread until the thread being
joined has completed its execution.

setPriority(int newPriority): Changes the priority of the thread. The priority is a
value between Thread.MIN_PRIORITY (1) and Thread.MAX_PRIORITY (10).

yield(): Thread.yield() is a static method that suggests the current thread
temporarily pause its execution to allow other threads of the same or higher
priority to execute. It’s important to note that yield() is just a hint to the
thread scheduler, and the actual behavior may vary depending on the JVM and OS.

Thread.setDaemon(boolean): Marks the thread as either a daemon thread or a user thread.
When the JVM exits, all daemon threads are terminated.

*/
class MyThread extends Thread {
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        // System.out.println(Thread.currentThread().getName() + " - State: " + Thread.currentThread().getState()); // Running state is within run()
        System.out.println(Thread.currentThread().getName() + " is Running...  Priority: " + Thread.currentThread().getPriority());
        try {
            Thread.sleep(1000); // Introduce a sleep to demonstrate TIMED_WAITING
            Thread.yield(); // Pause current Thread
            System.out.println(Thread.currentThread().getName() + " has yielded... Priority: " + Thread.currentThread().getPriority());
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
        }
        System.out.println(Thread.currentThread().getName() + " is completed.");
    }

}

class ThreadMethods {

    public static void main(String[] args) throws InterruptedException {

        MyThread lowPriorityThread = new MyThread("Low Priority Thread");
        // System.out.println(lowPriorityThread.getName() + " - State: " + lowPriorityThread.getState()); // NEW
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY);
        System.out.println(lowPriorityThread.getName() + " - State after setting priority: " + lowPriorityThread.getState()); // Still NEW
        lowPriorityThread.setDaemon(true); // lowPriorityThread is a daemon thread (like Garbage Collector) now.
        lowPriorityThread.start();
        System.out.println(lowPriorityThread.getName() + " - State after start(): " + lowPriorityThread.getState()); // RUNNABLE or may be TIMED_WAITING
        Thread.sleep(100);
        System.out.println(lowPriorityThread.getName() + " - State after sleep(100): " + lowPriorityThread.getState()); // TIMED_WAITING
        lowPriorityThread.join(); //main thread waits for lowPriorityThread to complete.
        System.out.println(lowPriorityThread.getName() + " - State after join(): " + lowPriorityThread.getState() + "------\n"); // TERMINATED

        MyThread mediumPriorityThread = new MyThread("Medium Priority Thread");
        // System.out.println("\n" + mediumPriorityThread.getName() + " - State: " + mediumPriorityThread.getState()); // NEW
        mediumPriorityThread.setPriority(Thread.NORM_PRIORITY);
        System.out.println(mediumPriorityThread.getName() + " - State after setting priority: " + mediumPriorityThread.getState()); // Still NEW
        mediumPriorityThread.start();
        System.out.println(mediumPriorityThread.getName() + " - State after start(): " + mediumPriorityThread.getState()); // RUNNABLE or TIMED_WAITING
        Thread.sleep(100);
        System.out.println(mediumPriorityThread.getName() + " - State after sleep(100): " + mediumPriorityThread.getState()); // TIMED_WAITING
        mediumPriorityThread.join();
        System.out.println(mediumPriorityThread.getName() + " - State after join(): " + mediumPriorityThread.getState() + "------\n"); // TERMINATED

        MyThread highPriorityThread = new MyThread("High Priority Thread");
        // System.out.println("\n" + highPriorityThread.getName() + " - State: " + highPriorityThread.getState()); // NEW
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);
        System.out.println(highPriorityThread.getName() + " - State after setting priority: " + highPriorityThread.getState()); // Still NEW
        highPriorityThread.start();
        System.out.println(highPriorityThread.getName() + " - State after start(): " + highPriorityThread.getState()); // RUNNABLE or TIMED_WAITING
        Thread.sleep(100);
        System.out.println(highPriorityThread.getName() + " - State after sleep(100): " + highPriorityThread.getState()); // TIMED_WAITING
        highPriorityThread.join();
        System.out.println(highPriorityThread.getName() + " - State after join(): " + highPriorityThread.getState() + "------\n"); // TERMINATED

        System.out.println("\n======Main Done========");

    }
}


/*
Low Priority Thread - State after setting priority: NEW
Low Priority Thread - State after start(): RUNNABLE
Low Priority Thread is Running...  Priority: 1
Low Priority Thread - State after sleep(100): TIMED_WAITING
Low Priority Thread has yielded... Priority: 1
Low Priority Thread is completed.
Low Priority Thread - State after join(): TERMINATED------

Medium Priority Thread - State after setting priority: NEW
Medium Priority Thread is Running...  Priority: 5
Medium Priority Thread - State after start(): RUNNABLE
Medium Priority Thread - State after sleep(100): TIMED_WAITING
Medium Priority Thread has yielded... Priority: 5
Medium Priority Thread is completed.
Medium Priority Thread - State after join(): TERMINATED------

High Priority Thread - State after setting priority: NEW
High Priority Thread is Running...  Priority: 10
High Priority Thread - State after start(): RUNNABLE
High Priority Thread - State after sleep(100): TIMED_WAITING
High Priority Thread has yielded... Priority: 10
High Priority Thread is completed.
High Priority Thread - State after join(): TERMINATED------


======Main Done========


*/
