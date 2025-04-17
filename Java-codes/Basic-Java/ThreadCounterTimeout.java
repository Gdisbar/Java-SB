
import java.util.concurrent.locks.ReentrantLock;

class SharedCounter{
	private static volatile int count = 0; // visible to all threads
	private final ReentrantLock lock = new ReentrantLock();
	protected int increment(){
		try{
			lock.lock();
			count++;
			//System.out.println("Thread : "+Thread.currentThread().getName()+" Count : "+count);
			return count;
		}catch(Exception e){
			System.err.println("Resource busy : SharedCounter()");
			return 0;
		}finally{
			lock.unlock();
		}
	}
	protected int decrement(){
		try{
			lock.lock();
			count--;
			//System.out.println("Thread : "+Thread.currentThread().getName()+" Count : "+count);
			return count;
		}catch(Exception e){
			System.err.println("Resource busy : SharedCounter()");
			return 0;
		}finally{
			lock.unlock();
		}
	}
}

class ThreadCounterDecrement{
	static class ThreadCounterDecrementRunnable implements Runnable{
		private SharedCounter sharedCounter;
		protected ThreadCounterDecrementRunnable(SharedCounter sharedCounter){
			this.sharedCounter = sharedCounter;
		}
		@Override
		public void run(){
			System.out.println(Thread.currentThread().getName()+" Count decremented : "+sharedCounter.decrement());
		}
	}
}

class ThreadCounterTimeout{
	public static void main(String[] args)throws InterruptedException{
		Thread[] threads = new Thread[6];
		try{
			SharedCounter sharedCounter = new SharedCounter(); // single instance
			for (int i=0;i<6 ;i++ ) {
				// System.out.println("Thread-" + (i + 1));
				if (i%2==0){
					// SharedCounter incrementCounter = new SharedCounter();
					// ThreadCounterIncrement.ThreadCounterIncrementRunnable incrementTask = new ThreadCounterIncrement.ThreadCounterIncrementRunnable(incrementCounter);
					threads[i] = new Thread(()-> {
						System.out.println(Thread.currentThread().getName()+" Count incremented : "+sharedCounter.increment());
					});
				}else{
					// SharedCounter decrementCounter = new SharedCounter();
					ThreadCounterDecrement.ThreadCounterDecrementRunnable decrementTask = new ThreadCounterDecrement.ThreadCounterDecrementRunnable(sharedCounter);
					threads[i] = new Thread(decrementTask);
					if(i-1>0){
						threads[i-1].join();
						System.out.println(threads[i].getName() + " joined. State: " + threads[i].getState());
					}
				}
				threads[i].start();
				System.out.println(threads[i].getName() + " started. State: " + threads[i].getState());
		    	System.out.println("--------------------------------");
		    }
		}catch(InterruptedException e){
			System.err.println("Error in main() thread "+e.getMessage());
		}finally{
			System.out.println("=====================================");
        	System.out.println("All threads finished.");
		}
	}

}

/*

you're creating a new instance of SharedCounter for each thread, meaning each thread has 
its own lock and count variable.

So even though count is static and shared, the lock is not shared, defeating the purpose of 
ReentrantLock.


✅ Keep return inside the try block.

❌ Never put return in the finally block.

Make SharedCounter a shared instance for all threads if you want correct concurrent 
updates to count.

*/