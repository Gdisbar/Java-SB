package List;

import java.util.Vector;

public class VectorBasic {
    public static void main(String[] args) {

        Vector<Integer> list = new Vector<>(5, 3);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 17; i++) {
                list.add(i);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 22; i++) {
                list.add(i);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Size of list: " + list.size()); // Output: 2000
        System.out.println("Capacity : "+list.capacity());
        System.out.println("(Capacity-5)%3 : "+(list.capacity()-5)%3);
//        Vector<Integer> vector1 = new Vector<>(list);
//        vector1.clear();


    }
}
