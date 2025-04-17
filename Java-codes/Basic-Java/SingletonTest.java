// Eager Initialization (Thread-Safe)
class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {
        // Private constructor to prevent instantiation from outside
    }

    public static EagerSingleton getInstance() {
        return instance;
    }

    public void showMessage() {
        System.out.println("Eager Singleton Instance");
    }
}

// Lazy Initialization (Thread-Safe using synchronized method)
class LazySingletonSynchronizedMethod {
    private static LazySingletonSynchronizedMethod instance;

    private LazySingletonSynchronizedMethod() {
        // Private constructor
    }

    public static synchronized LazySingletonSynchronizedMethod getInstance() {
        if (instance == null) {
            instance = new LazySingletonSynchronizedMethod();
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Lazy Singleton (Synchronized Method) Instance");
    }
}

// Lazy Initialization (Thread-Safe using Double-Checked Locking)
class LazySingletonDoubleCheck {
    private static volatile LazySingletonDoubleCheck instance; // volatile for memory visibility

    private LazySingletonDoubleCheck() {
        // Private constructor
    }

    public static LazySingletonDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (LazySingletonDoubleCheck.class) {
                if (instance == null) {
                    instance = new LazySingletonDoubleCheck();
                }
            }
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Lazy Singleton (Double-Checked Locking) Instance");
    }
}

// Lazy Initialization (Thread-Safe using Static Inner Class)
class LazySingletonStaticInner {

    private LazySingletonStaticInner() {
        // Private constructor
    }

    private static class SingletonHelper {
        private static final LazySingletonStaticInner INSTANCE = new LazySingletonStaticInner();
    }

    public static LazySingletonStaticInner getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void showMessage() {
        System.out.println("Lazy Singleton (Static Inner Class) Instance");
    }
}

public class SingletonTest {
    public static void main(String[] args) {
        EagerSingleton eager = EagerSingleton.getInstance();
        eager.showMessage();

        LazySingletonSynchronizedMethod lazySync = LazySingletonSynchronizedMethod.getInstance();
        lazySync.showMessage();

        LazySingletonDoubleCheck lazyDoubleCheck = LazySingletonDoubleCheck.getInstance();
        lazyDoubleCheck.showMessage();

        LazySingletonStaticInner lazyInner = LazySingletonStaticInner.getInstance();
        lazyInner.showMessage();
    }
}


/**
 * Comparison:

Eager Initialization:

Thread Safety: Inherently thread-safe. The instance is created when the class is loaded, which is guaranteed to be thread-safe by the JVM.
Performance: Slightly less performant in terms of memory usage if the singleton is never used, as it's created regardless. However, it's generally faster for subsequent calls to getInstance() because there's no synchronization overhead.
Simplicity: Very simple to implement.


Lazy Initialization (Synchronized Method):

Thread Safety: Thread-safe, but with significant performance overhead due to the synchronized keyword on the getInstance() method. Every call to getInstance() acquires a lock, even after the instance has been created.
Performance: Slower than eager initialization, especially in highly concurrent environments.
Simplicity: Relatively simple to implement.
Lazy Initialization (Double-Checked Locking):

Thread Safety: Thread-safe when implemented correctly (using volatile for the instance variable). The double-check reduces the overhead of synchronization.
Performance: Better performance than the synchronized method approach, but can still have some overhead.
Complexity: Slightly more complex to implement correctly. Requires careful use of volatile.
Prior to Java 5, Double-Checked Locking had issues with some JVM implementations. Volatile was not properly implemented and could allow race conditions. Java 5 and later fixes this issue.

Lazy Initialization (Static Inner Class):

Thread Safety: Inherently thread-safe. The inner class is loaded only when getInstance() is called, and the instance is created only once.
Performance: Good performance, as it avoids unnecessary synchronization.
Simplicity: Relatively simple and considered the best practice for lazy initialization.
Recommended approach: This is the best approach for lazy initialization. It combines lazy loading with thread safety without requiring explicit locking.

Which to Choose:

If the singleton is always needed, use eager initialization for simplicity and performance.
If lazy loading is essential, use the static inner class approach. It offers the best combination of thread safety, performance, and simplicity.
Avoid the synchronized method approach due to performance penalties.
Double checked locking is effective, but the static inner class approach is generally simpler and safer.
 * 
 * **/