package Hash;

import java.util.*;

public class SortedMapBasic {
    public static void main(String[] args) {
        SortedMap<Integer, String> map = new TreeMap<>((a, b) -> b - a);
        map.put(91, "Vivek");
        map.put(99, "Shubham");
        map.put(78, "Mohit");
        map.put(77, "Vipul");
        map.get(77);
        System.out.println("containsKey(78) : "+map.containsKey(78));
        System.out.println("containsValue(77) : "+map.containsValue(77));
        System.out.println("headMap(91) : "+map.headMap(91)); // exclude
        System.out.println("tailMap(91) : "+map.tailMap(91));
//        System.out.println(map.firstKey());
//        System.out.println(map.lastKey());
        System.out.println("SortedMap : "+map);
        System.out.println("=====================");

        // SortedMap + extra methods = NavigableMap

        NavigableMap<Integer, String> navigableMap =  new TreeMap<>();
        navigableMap.put(1, "One");
        navigableMap.put(5, "Five");
        navigableMap.put(3, "Three");
        System.out.println("NavigableMap : "+navigableMap);
        System.out.println("headMap(5) : "+navigableMap.headMap(5)); // exclude
        System.out.println("tailMap(5) : "+navigableMap.tailMap(5));
        System.out.println("lowerKey(4) : "+navigableMap.lowerKey(4));
        System.out.println("ceilingKey(3) : "+navigableMap.ceilingKey(3));
        System.out.println("higherEntry(1) : "+navigableMap.higherEntry(1));
        System.out.println("descendingMap() : "+navigableMap.descendingMap());
        // IdentityHashMap can store duplicate Key
        Map<String, Integer> identityHashMap = new IdentityHashMap<>();
        String key1 = new String("Akshit");
        String key2 = new String("Akshit");
        identityHashMap.put(key1, 90);
        identityHashMap.put(key2, 92);
        System.out.println("IdentityHashMap : "+identityHashMap);
        // ImmutableMap -- Map.of(),Map.ofEntries()
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);
//        Map<String, Integer> map2 = Collections.unmodifiableMap(map1);
//        System.out.println(map2);
//        map2.put("C", 3); throws exception

        try {
            Map<String, Integer> map3 = new HashMap<>(Map.of("Shubham", 98, "Vivek", 89));
//            Map<String, Integer> map3 = Map.of("Shubham", 98, "Vivek", 89);
            System.out.println("Map.of() : "+map3);
            // without new HashMap<>(Map.of(k,v,k,v)) will create exception in below lines
            map3.put("Akshit", 88);
            System.out.println("HashMap after put() : " + map3);
            Map<String, Integer> map4 = Map.ofEntries(Map.entry("Akshit", 99), Map.entry("Vivek", 99));
            System.out.println("Map.ofEntries() : "+map4);
        }catch (UnsupportedOperationException e){
            e.printStackTrace();
        }
    }
}
