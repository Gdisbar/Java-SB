// import java.util.concurrent.atomic.AtomicInteger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.TimeoutException; 
import java.util.List;
import java.util.ArrayList; 

class SharedCounter{
	private static volatile int count = 0; // visible accrosss all thread
	private final ReentrantLock lock = new ReentrantLock();
	protected int increment(){
		try{
			lock.lock();
			count++;
			return count; // should I put it after or before lock.unlock();
		}catch(Exception e){
			System.err.println("Resource busy .... ");
			return count; // should I put it after or before lock.unlock();
		}finally{
			lock.unlock();
		}

	}
}

class Task implements Runnable{
	private SharedCounter sharedCounter;
	protected Task(SharedCounter sharedCounter){
		this.sharedCounter = sharedCounter;
	}
	@Override
	public void run(){
		System.out.println(Thread.currentThread().getName()+" "+Thread.currentThread().getState()+" Counter : "+sharedCounter.increment());
	}
}

class CallIncrement implements Callable<Integer>{
	private final Integer startCounter;
	private final Integer expiryCounter;
	protected CallIncrement(final int startCounter,final int expiryCounter){
		this.startCounter = Integer.valueOf(startCounter);
		this.expiryCounter = Integer.valueOf(expiryCounter);
	}
	public Integer call(){
		int counter = Integer.parseInt(startCounter.toString());
		try{
			for (int i=1;i<=Integer.parseInt(expiryCounter.toString()) ;i++ ) {
				counter+=1;
				TimeUnit.MILLISECONDS.sleep(500); // Simulate some work
			}
		}catch(InterruptedException e){
			System.err.println("InterruptedException in CallIncrement() "+e.getMessage());
			// throw e;
		}
		return Integer.valueOf(counter);
	}
}

class TimerThread{
	public static void main(String[] args){
		List<Callable<Integer>> tasks = List.of(new CallIncrement(1, 20)); // increase the expiry counter to ensure timeout.
		try(ExecutorService executor = Executors.newFixedThreadPool(1);){
			List<Future<Integer>> futures = executor.invokeAll(tasks, 3, TimeUnit.SECONDS);
			Future<Integer> future = futures.get(0);
			try{
				Integer result = future.get(0, TimeUnit.SECONDS); // Use get with a timeout
                System.out.println("Task completed successfully. Result: " + result);
			}catch (TimeoutException e) {
                System.out.println("Task timed out.");
                future.cancel(true); // Cancel the task
            }catch (ExecutionException e) {
                if (e.getCause() instanceof CancellationException) {
                    System.out.println("Task was cancelled.");
                } else {
                    System.err.println("ExecutionException during task execution: " + e.getMessage());
                }
            }catch (Exception e) {
                System.err.println("Exception during task execution: " + e.getMessage());
            }
            executor.shutdown(); // Prevent new tasks from being accepted
			// for (int i = 1; i <= 6; i++) {
            // 	executor.execute(new Task(i));
        	// }
		}catch(InterruptedException e){
			System.err.println("InterruptedException in main()"+e.getMessage());
		}
	}
}