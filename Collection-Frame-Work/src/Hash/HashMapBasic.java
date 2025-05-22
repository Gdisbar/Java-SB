package Hash;


import java.util.*;

/*
If we don't override equals() and hashCode() method for a custom
Object then due to hashCode being different new value will be added
to the node rather than updating the old value (which will increase
the HashMap size)
* */

public class HashMapBasic {
    public static void main(String[] args) {
//        System.out.println("Apple : "+linkedHashMap.get("Apple"));
        HashMap<Person, String> map = new HashMap<>();
        Person p1 = new Person("Alice", 1);
        Person p2 = new Person("Bob", 2);
        Person p3 = new Person("Alice", 1);

        map.put(p1, "Engineer"); // hashcode1 --> index1
        map.put(p2, "Designer"); // hashcode2 --> index2
        map.put(p3, "Manager"); // hashcode1 --> index1 --> equals() --> replace

        System.out.println("HashMap Size: " + map.size());
        System.out.println("Value for p1: " + map.get(p1));
        System.out.println("Value for p3: " + map.get(p3));

        System.out.println("==================================");
        LinkedHashMap<Integer,String> map2 = new LinkedHashMap<>(3, 0.3f, true); // double linked list
//        HashMap<Integer, String> map2 = new HashMap<>(17,0.5f);
        map2.put(31, "Shubham");
        map2.put(11, "Akshit");
        map2.put(2, "Neha");
        map2.put(2, "Mehul");

        Set<Map.Entry<Integer,String>> entries = map2.entrySet();
        for (Map.Entry<Integer,String> entry : entries){
            System.out.println("Key :: "+entry.getKey()+" value :: "+ entry.getValue());
        }
        System.out.println("-----------------");
        String student = map2.get(31);
        System.out.println("value of key 31 "+student);
        String s1 = map2.get(69);
        String s2 = map2.getOrDefault(69, "No key found");
        System.out.println("get(69) (key don't exist) : "+s1);
        System.out.println("getOrDefault( 69, \"No key found\")  : "+s2);

        System.out.println("containsKey(2) : "+map2.containsKey(2));
        System.out.println("containsValue(\"Shubham\") : "+map2.containsValue("Shubham"));

        boolean res = map2.remove(31, "Nitin");
        System.out.println("REMOVED ? : " + res);

        // this will remove the previous value associated with key,
        // or null if there was no mapping for key. (A null return can also indicate that the map
        // previously associated null with key.)
        String res2 = map2.remove(31);
        System.out.println("REMOVED ? : " + res2);

    }
}

class Person {
    private String name;
    private int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Person other = (Person) obj;
        return id == other.getId() && Objects.equals(name, other.getName());
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
    }

}
