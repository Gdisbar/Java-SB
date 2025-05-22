/*
INSERTION METHODS
=========================================================================================
addFirst(E e): Inserts the specified element at the front,
addLast(E e): Inserts the specified element at the end
offerFirst(E e): Inserts the specified element at the front if possible.
offerLast(E e): Inserts the specified element at the end if possible.

for addFirst(E e),addLast(E e) -> ArrayBlockingQueue<>(capacity) throws exception
if queue.size() > capacity

REMOVAL METHODS
=========================================================================================
removeFirst(): Retrieves and removes the first element or throws no such element exception
removeLast(): Retrieves and removes the last element or throws exception
pollFirst(): Retrieves and removes the first element, or returns null if empty.
pollLast(): Retrieves and removes the last element, or returns null if empty.

EXAMINATION METHODS
===============================================================================
getFirst(): Retrieves, but does not remove, the first element, throws exception
getLast(): Retrieves, but does not remove, the last element, throws exception
element() : similar to getFirst(), throws exception if queue is empty
peekFirst(): Retrieves, but does not remove, the first element, or returns null if empty.
peekLast(): Retrieves, but does not remove, the last element, or returns null if empty.

STACK METHODS
===============================
push(E e): Adds an element at the front (equivalent to addFirst(E e)).
pop(): Removes and returns the first element (equivalent to removeFirst())..

*/

// part of the Queue interface
// orders elements based on their natural ordering (for primitives lowest first)
// custom comparator for customised ordering
// does not allow null elements
// internal working
// PriorityQueue is implemented as a min-heap by default (for natural ordering)


package Queue;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class DequeBasic {
    public static void main(String[] args) {
        Deque<Integer> deque1 = new ArrayDeque<>(); // faster iteration, low memory, no null allowed
        // circular, head and tail
        // no need to shift elements, just shift head and tail
//        deque1.addFirst(10); // head--
//        deque1.addLast(20);
        deque1.offerFirst(5);
        deque1.offerLast(25);
        System.out.println(deque1);         // 5, 10, 20, 25
        System.out.println("First Element: " + deque1.getFirst()); // Outputs 5
        System.out.println("Last Element: " + deque1.getLast());   // Outputs 25
        deque1.removeFirst(); // Removes 5
        deque1.pollLast();    // Removes 25
//        deque1.removeFirst();
//        System.out.println("First Element: " + deque1.getFirst()); // throws exception
        // Current Deque: [10, 20]
        for (int num : deque1) {
            System.out.println("Current Deque : "+num);
        }
        //===================================================
        Queue<Integer> queue1 = new LinkedList<>();
        queue1.add(1);
        queue1.add(2);
        System.out.println("queue1.size() : "+queue1.size());

        System.out.println("queue1.remove(1) throws exception if empty : "+queue1.remove(1)); // throws exception if empty
        System.out.println("queue1.poll() : "+queue1.poll());
//        System.out.println("queue1.element() throws exception if empty : "+queue1.element());  // throws exception if empty
        System.out.println("queue1.peek() : "+queue1.peek());
        System.out.println("========== ArrayBlockingQueue of capacity 2============");
        Queue<Integer> queue2 =  new ArrayBlockingQueue<>(2);
        System.out.println("queue2.add(1) : "+queue2.add(1)); // true
        System.out.println("queue2.offer(2) : "+queue2.offer(2)); // true
//      System.out.println("size > capacity -> queue.add(e) throws exception: "+queue2.add(3)); // throws exception
        System.out.println("queue2.offer(3) : "+queue2.offer(3)); // false
        //==========================================================================
        PriorityQueue<Integer> pq = new PriorityQueue<>((a,b)->b-a);
        pq.add(15);
        pq.add(10);
        pq.add(30);
        pq.add(5);
        System.out.println("not sorted (max-heap) : "+pq); // not sorted - [30, 10, 15, 5]
        while (!pq.isEmpty()){
            System.out.println("pq.poll() : "+pq.poll());
        }


    }
}
