
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class ProduceConsume{
	private final Queue<Integer> buffer;
	private final int capacity;
	protected ProduceConsume(int capacity){
		this.capacity = capacity;
		this.buffer = new LinkedList<>();
	}
	protected void produce(int value)throws InterruptedException{
		synchronized(this){
			if(buffer.size()==capacity){
				wait();
			}
			buffer.offer(value);
			notifyAll(); // Notify consumers that a value is available
			System.out.println("Produced : "+value);

		}
	}
	protected int consume()throws InterruptedException{
		synchronized(this){
			if(buffer.isEmpty()){
				wait();
			}
			int value = buffer.poll();
            notifyAll();
            System.out.println("Consumed: " + value);
            return value;
		}
	}

}

class ProducerConsumer{  
	public static void main(String[] args){

			ExecutorService executor = Executors.newCachedThreadPool();
			ProduceConsume pc = new ProduceConsume(5);
			for (int i=0;i< 10 ;i++ ) {
				int value = i+1;
				executor.submit(() -> {
					try{					// catch Exception thrown by ProduceConsume
						pc.produce(value);
					}catch(InterruptedException e){  
						Thread.currentThread().interrupt(); // Restore interrupted status
					}
				});
			}
			for (int i=0;i < 10 ;i++) {
				executor.submit(() -> {
					try{					// catch Exception thrown by ProduceConsume
						pc.consume();
					}catch(InterruptedException e){  
						Thread.currentThread().interrupt(); // Restore interrupted status
					}
				});
			}
		executor.shutdown();
	}
}

/*
The lambda expression () -> { pc.produce(value); ... } can access pc and value because 
it captures them from the main method's scope. This is a fundamental feature of lambda 
expressions in Java.

To be captured by a lambda, local variables from the enclosing scope must be effectively final. 
This means that their values cannot be changed after they are first assigned.

If you try to change the value of pc or value after their initial assignment within 
the loop, you will get a compilation error.

*/