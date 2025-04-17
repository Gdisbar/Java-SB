import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;

class ThreadSafeCounter {
    // private Object item;
    // private AtomicInteger count = new AtomicInteger(0);
    // private ConcurrentHashMap<Object, AtomicInteger> counterMap;
    // protected ThreadSafeCounter(Object item){
    // 	this.item = item;
    // 	this.counterMap = new ConcurrentHashMap<>();
    // }
    
    // protected void increment(Object item) {
    //     count.incrementAndGet();  // Atomic operation
    //     counterMap.put(item,count);
    //     System.out.println("inserted Object into "+item.getClass().getName());
    // }

    private ConcurrentHashMap<Object, AtomicInteger> counterMap = new ConcurrentHashMap<>();

    public void increment(Object item) {
        counterMap.compute(item, (key, oldValue) -> {
            if (oldValue == null) {
                return new AtomicInteger(1);
            } else {
                oldValue.incrementAndGet();
                return oldValue;
            }
        });
    }
    
    protected int getCount(Object item) {
        AtomicInteger count = counterMap.get(item);
        return (count != null) ? count.get() : 0;
    }
}

class ThreadSafeCounterMap{
	public static void main(String[] args) {
		// ThreadSafeCounter th1 = new ThreadSafeCounter("item-1");
		// ThreadSafeCounter th2 = new ThreadSafeCounter("item-2");
		// ThreadSafeCounter th3 = new ThreadSafeCounter("item-3");
		// th1.increment("item-1");
		// th2.increment("item-2");
		// th3.increment("item-3");
		// th2.increment("item-2");
		// th2.increment("item-2");
		// th2.increment("item-2");
		// th3.increment("item-3");
		// System.out.println("item-1 count: "+th1.getCount("item-1"));
		// System.out.println("item-2 count: "+th2.getCount("item-2"));
		// System.out.println("item-3 count: "+th3.getCount("item-3"));

		ThreadSafeCounter counterMap = new ThreadSafeCounter();

        counterMap.increment("item-1");
        counterMap.increment("item-2");
        counterMap.increment("item-3");
        counterMap.increment("item-2");
        counterMap.increment("item-2");
        counterMap.increment("item-2");
        counterMap.increment("item-3");

        System.out.println("item-1 count: " + counterMap.getCount("item-1"));
        System.out.println("item-2 count: " + counterMap.getCount("item-2"));
        System.out.println("item-3 count: " + counterMap.getCount("item-3"));
	}
}