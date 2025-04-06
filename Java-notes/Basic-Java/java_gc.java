

========================================================================================
Java Memory Management and Garbage Collection
========================================================================================

+--------------------------------------------+
|                    JVM                    |
+---------------------+---------------------+
|      Method Area     |       Heap          |
|  - Class Metadata    |  - Objects          |
|  - Method Code       |  - Strings          |
|  - Static Variables  |                     |
+---------------------+---------------------+
|                     Stack                  |
|  - Method Calls                            |
|  - Local Variables                         |
|  - Return Addresses                        |
+--------------------------------------------+
|               Native Method Stack          |
+--------------------------------------------+
|               PC Register                  |
+--------------------------------------------+



Method Area (Part of Heap)
--------------------------------------------
- Stores class-level metadata like:
	Class name
	Method and field information
	Constant pool (like string literals)
- Shared among all threads.
- Memory allocated at class loading time.

Heap (Main Memory Area)
----------------------------------------------
- Largest part of JVM memory.
- Stores objects and instance variables.
- Garbage Collector operates here.
- Divided into:
	Young Generation → Newly created objects.
	Old Generation → Long-lived objects.
	Permanent Generation (Metaspace) → Class definitions and metadata.

                +-----------------------+
                |       Young Gen       |
                |  +-------+-------+   |
                |  | Eden  | S0 | S1 | |
                +-----------------------+
                |       Old Gen         |
                +-----------------------+
                |       Metaspace       |
                +-----------------------+


Stack
-----------------------------------------------
- Stores:
	Method calls (frames)
	Local variables
	Return values
- Created per thread → Thread-safe
- Last-In-First-Out (LIFO) structure


Native Method Stack
--------------------------------------------------------------
- Used for native methods (non-Java code) like C or C++ functions.

PC Register (Program Counter Register)
---------------------------------------------------------------------
- Stores the address of the current executing instruction in the thread.
- Each thread has its own PC register.

Memory Allocation Strategy (When an object is created)
------------------------------------------------------------------------
JVM checks in Eden Space → If space available → Allocates object
If Eden Space is full → Minor GC happens
If object survives several Minor GCs → Moved to Survivor Space
If object survives for long → Moved to Old Generation


Common Causes of Memory Leaks
-------------------------------------------------------------------------
Unclosed resources → Sockets, Files, Streams
---------------------------------------------------------------
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("data.txt"));
    String line;
    while ((line = reader.readLine()) != null) {
        // Process the line
        System.out.println(line);
    }
    // Oops! Forgot to close the reader!
} catch (IOException e) {
    e.printStackTrace();
} finally {
    // Correct way to close the resource
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

Uncleared static references
------------------------------------------------------------------
private static List<String> dataCache = new ArrayList<>();

    public static void loadData(String item) {
        dataCache.add(item);
        // No mechanism to remove items from dataCache
    }

// inside main
for (int i = 0; i < 100000; i++) {
    loadData("Item " + i);
}
// dataCache will keep growing, potentially leading to an OutOfMemoryError


Inner classes holding outer class references
------------------------------------------------
public class OuterClass {
    private String outerData;

    public OuterClass(String data) {
        this.outerData = data;
        new InnerClass(); // Creating an instance of the inner class
    }

    public class InnerClass {
        public void displayOuterData() {
            System.out.println("Outer data: " + outerData);
        }
    }

// inside main
for (int i = 0; i < 1000; i++) {
    new OuterClass("Data " + i);
    // The InnerClass instances hold references to the OuterClass instances,
    // even though the OuterClass instances might not be needed anymore.
}
    
}


Listener objects not removed
Incorrect caching
------------------------------------------------------------------------
private static Map<String, Object> cache = new HashMap<>();

public static void storeData(String key, Object data) {
    cache.put(key, data);
    // No mechanism to remove items from the cache
}

public static Object getData(String key) {
    return cache.get(key);
}

// main method
for (int i = 0; i < 100000; i++) {
    storeData("Key " + i, new byte[1024]); // Storing some data
}
// The cache will keep growing, potentially leading to an OutOfMemoryError



Garbage Collection (GC) in Java
---------------------------------------------------------------------------------------
Eligible for GC: An object becomes eligible for GC when no active thread can access it.
GC Roots: Objects that are never garbage collected (e.g., static references, active thread references).

Reference Types:
---------------------------------------------------------------------------------------
Strong References: Prevent GC. (Default behavior)
Weak References: Collected when GC runs.
Soft References: Collected only if memory is low.
Phantom References: Final cleanup before object deletion.

Working of GC 
-----------------------------------------------------------------------
JVM runs the Garbage Collector (GC) in the background.
GC identifies objects with no live references and reclaims their memory.
- GC uses "Mark and Sweep" algorithm:
	Mark → Identify objects that are still in use.
	Sweep → Remove unreferenced objects from memory.
	Compact → Reorganize memory to reduce fragmentation.

Types of Garbage Collectors
-----------------------------------------------------------------------
GC Type			Description								Best Use Case
------------------------------------------------------------------------
Serial GC	-> Uses a single thread (stop-the-world)	-> Single-threaded applications
Parallel GC	-> Uses multiple threads for GC	            -> Multi-threaded applications
CMS (Concurrent Mark-Sweep) GC	-> Runs alongside application threads	->Low-latency applications
G1 (Garbage First) GC			->Divides heap into regions	            ->Large heaps (JVM default)
ZGC (Z Garbage Collector)	    ->Handles large heaps with minimal pause time	->Very large heap sizes (>10 GB)


GC Roots
---------------------------------------------
Garbage Collector starts from GC roots:

Local variables in stack
Static class variables
Active threads
JNI references

public class GarbageCollectionExample {
    public static void main(String[] args) {
        GarbageCollectionExample obj = new GarbageCollectionExample();
        obj = null; // Eligible for GC

        System.gc(); // Request for Garbage Collection
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Garbage collected!");
    }
}




Memory Optimization Techniques
----------------------------------------------------------------------------
✅ Use StringBuilder instead of String for concatenation.
✅ Close database connections, streams, and files in the finally block.
✅ Use WeakReference for cache management.
✅ Use ThreadLocal for thread safety.
✅ Avoid large object creation in loops.

Tools for Monitoring Memory and GC
------------------------------------------------------------------------------
jconsole    			->GUI-based tool for monitoring memory and GC
VisualVM				->Profiling tool to analyze memory and CPU usage
jstat					->Command-line tool to monitor GC
Java Mission Control	->Provides in-depth JVM performance data

