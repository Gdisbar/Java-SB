/**
 * 
 * Synchronization Does NOT Guarantee Execution Order

Synchronization's Purpose:
Synchronization (using synchronized blocks or other locking mechanisms) is primarily designed to control access to shared resources and prevent race conditions.
It ensures that only one thread can execute a critical section of code at a time, maintaining data integrity.
No Order Guarantee:
Synchronization does not guarantee any specific order of thread execution. The operating system's thread scheduler is responsible for determining which thread runs at any given moment.
The scheduler can preempt threads, switch between them, and prioritize them based on various factors (e.g., priority, system load).
What You WILL See:
You will see that the creation of the singleton object itself, which is the protected resource, is only created once.
You will see that the output of the doSomething method will be consistent.
You will see that the threads will all complete their execution.
What You WON'T See:
You will not see that the threads run in the order of the thread array.
You will not see that the threads run one at a time, in a sequential manner.

 * 
 * **/




class ThreadSafeSingleton {
    private static volatile ThreadSafeSingleton instance; // Volatile for visibility

    private ThreadSafeSingleton() {
        // Private constructor to prevent instantiation from outside
    }

    public static ThreadSafeSingleton getInstance() {
        if (instance == null) { // First check: avoid unnecessary synchronization
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) { // Second check: ensure only one instance is created
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }

    public String doSomething(String message) {
        message = message.concat(" : doSomething() running...");
        return message;
    }
}

class ThreadSingleton {

        static class SingletonRunnable implements Runnable{
            private ThreadSafeSingleton singleton;
            private String message;
            protected SingletonRunnable(ThreadSafeSingleton singleton,String message) {
                this.singleton = singleton;
                this.message = message;
            }
            @Override
            public void run(){
                System.out.println(Thread.currentThread().getName() + ": " + singleton.doSomething(message));
            }
        }
    public static void main(String[] args)throws InterruptedException {

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            ThreadSafeSingleton singleton = ThreadSafeSingleton.getInstance();
            String message = "Thread " + (i + 1) + " Message";
            threads[i] = new Thread(new SingletonRunnable(singleton, message), "Thread-" + (i + 1));
            System.out.println("Creating " + threads[i].getName() + " " + threads[i].getState());
        }

        System.out.println("=============");

        try {
            for (Thread thread : threads) {
                thread.start();
                System.out.println(thread.getName() + " started. State: " + thread.getState());
            }

            for (Thread thread : threads) {
                thread.join();
                System.out.println(thread.getName() + " joined. State: " + thread.getState());
            }

        } catch (InterruptedException e) {
            System.err.println("Exception in main() method " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception in main() method " + e.getMessage());
        }

        System.out.println("=============");
        System.out.println("All threads finished.");
        
    }
}