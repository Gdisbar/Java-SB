package Hash;

// Hashtable is synchronized
// no null key or value
// Legacy Class, ConcurrentHashMap
// slower than HashMap
// only linked list in case of collision
//  all methods are synchronized

/*
hashtable.put(1, "Apple");
System.out.println("Value for key 2: " + hashtable.get(2));
System.out.println("Does key 3 exist? " + hashtable.containsKey(3));
hashtable.remove(1);
*/

import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
public class EnumMapBasic {
    public static void main(String[] args) {
        // array of size same as enum
        // [_,"Gym",_,_,_,_,_]
        // no hashing
        // ordinal/index is used
        // FASTER THAN HASHMAP
        // MEMORY EFFICIENT
        Map<Day, String> map1 = new EnumMap<>(Day.class);
        map1.put(Day.TUESDAY, "Gym");
        map1.put(Day.MONDAY, "Walk");
        String s = map1.get(Day.TUESDAY);
        System.out.println(map1);

//        hashtable.put(null, "value"); // Throws exception
//        hashtable.put(4, null);   // Throws exception

        Hashtable<Integer, String> map = new Hashtable<>();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 11; i++) {
                map.put(i, "Thread1");
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 11; i < 20; i++) {
                map.put(i, "Thread2");
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final size of HashMap: " + map.size());
    }
}
