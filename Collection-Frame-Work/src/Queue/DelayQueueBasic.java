package Queue;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DelayedTask implements Delayed {

    private final String taskName;
    private final long startTime;

    public DelayedTask(String taskName, long delay, TimeUnit unit) {
        this.taskName = taskName;
        this.startTime = System.currentTimeMillis() + unit.toMillis(delay);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long remaining = startTime - System.currentTimeMillis();
        return unit.convert(remaining, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.startTime, ((DelayedTask) o).startTime);
    }

    public String getTaskName() {
        return taskName;
    }
}

public class DelayQueueBasic {
    public static void main(String[] args) {
        // Thread-safe unbounded blocking queue
        // Elements can only be taken from the queue when their delay has expired
        // Useful for scheduling tasks to be executed after a certain delay
        // internally priority queue
        BlockingQueue<DelayedTask> delayQueue = new DelayQueue<>();
        try {
            delayQueue.put(new DelayedTask("Task1", 5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            delayQueue.put(new DelayedTask("Task2", 3, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            delayQueue.put(new DelayedTask("Task3", 10, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (!delayQueue.isEmpty()) {
            DelayedTask task = null; // Blocks until a task's delay has expired
            try {
                task = delayQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Executed: " + task.getTaskName() + " at " + System.currentTimeMillis());
        }
    }
}
