
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
class Produce implements Runnable{
	private final ProduceConsume pc;
	private final int value;
	protected Produce(ProduceConsume pc,int value){
		this.value = value;
		this.pc = pc;
	}
	@Override
	public void run(){
		try{					// catch Exception thrown by ProduceConsume
			pc.produce(value);
		}catch(InterruptedException e){  
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
	}

}

class Consume implements Runnable{
	private final ProduceConsume pc;
	protected Consume(ProduceConsume pc){
		this.pc = pc;
	}
	@Override
	public void run(){
		try{
			pc.consume();
		}catch(InterruptedException e){
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
	}
}

class ProducerConsumer{
	public static void main(String[] args){

			ExecutorService executor = Executors.newCachedThreadPool();
			ProduceConsume pc = new ProduceConsume(5);
			for (int i=0;i< 10 ;i++ ) {
				executor.submit(new Produce(pc,i+1));
			}
			for (int i=0;i < 10 ;i++) {
				executor.submit(new Consume(pc));
			}
		executor.shutdown();
	}
}