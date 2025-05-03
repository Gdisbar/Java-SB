/*
Fork-Join Framework(introduced in Java 7)
-------------------------------------------------------------------------
Extends AbstractExecutorService.
Uses a work-stealing algorithm — idle threads "steal" work from busy threads. 
Splits the task recursively → Parallel execution + uses Divide-and-Conquer approach

ForkJoinTask<V>     ->Base class for fork-join tasks
RecursiveTask<V>    ->Returns a result
RecursiveAction     ->Does not return a result

✔️ Thread Pools → Best for independent tasks.
✔️ Fork-Join → Best for recursive tasks.
*/

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Arrays;

class BinarySearchTask extends RecursiveTask<Integer> {
    private int[] arr;
    private int target;
    private int start;
    private int end;

    public BinarySearchTask(int[] arr, int target, int start, int end) {
        this.arr = arr;
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (start > end) {
            return -1; // Target not found
        }

        int mid = (start + end) / 2;

        if (arr[mid] == target) {
            return mid; // Target found
        } else if (arr[mid] > target) {
            if (end - start <= 100){ //adjust this number based on array size and desired granularity.
                return sequentialSearch(arr, target, start, mid-1);
            }
            BinarySearchTask left = new BinarySearchTask(arr, target, start, mid - 1);
            return left.invoke();
        } else {
            if (end - start <= 100){
                return sequentialSearch(arr, target, mid+1, end);
            }
            BinarySearchTask right = new BinarySearchTask(arr, target, mid + 1, end);
            return right.invoke();
        }
    }

    private int sequentialSearch(int[] arr, int target, int start, int end){
        for(int i = start; i <= end; i++){
            if(arr[i] == target) return i;
        }
        return -1;
    }

    
}

class ForkJoinFrameWork{
    public static void main(String[] args) {
        int[] arr = new int[1000];
        for(int i = 0; i < 1000; i++){
            arr[i] = i * 2;
        }

        int target = 500;
        ForkJoinPool pool = new ForkJoinPool();
        BinarySearchTask task = new BinarySearchTask(arr, target, 0, arr.length - 1);
        int result = pool.invoke(task);

        if (result != -1) {
            System.out.println("Target found at index: " + result);
        } else {
            System.out.println("Target not found.");
        }
    }
}