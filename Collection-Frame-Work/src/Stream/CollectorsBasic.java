package Stream;

import java.util.*;
import java.util.stream.Collectors;

public class CollectorsBasic {
    public static void main(String[] args) {
        // Collectors is a utility class
        // provides a set of methods to create common collectors

        // 1. Collecting to a List
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<String> res = names.stream()
                .filter(name -> name.startsWith("A"))
                .collect(Collectors.toList());
        System.out.println(res); //[Alice]

        // 2. Collecting to a Set
        List<Integer> nums = Arrays.asList(1, 2, 2, 3, 4, 4, 5);
        Set<Integer> set = nums.stream().collect(Collectors.toSet());
        System.out.println(set); // [1, 2, 3, 4, 5]

        // 3. Collecting to a Specific Collection
        ArrayDeque<String> collect = names.stream().collect(Collectors.toCollection(() -> new ArrayDeque<>()));
        System.out.println("collect : "+collect); // collect : [Alice, Bob, Charlie]
        // 4. Joining Strings - concatenatedNames : ALICE, BOB, CHARLIE
        // Concatenates stream elements into a single String
        String concatenatedNames = names.stream().map(String::toUpperCase).collect(Collectors.joining(", "));
        System.out.println("concatenatedNames : "+concatenatedNames);

        List<Integer> numbers = Arrays.asList(2, 3, 5, 7, 11);
        // 5. Calculating Averages
        Double average = numbers.stream().collect(Collectors.averagingInt(x -> x));
        System.out.println("Average: " + average); // Average: 5.6

        // 6. Counting Elements
        Long count = numbers.stream().collect(Collectors.counting());
        System.out.println("Count: " + count); // Count: 5

        // 7. Grouping Elements
        List<String> words = Arrays.asList("hello", "world", "java", "streams", "collecting");
        // {4=[java], 5=[hello, world], 7=[streams], 10=[collecting]}
        System.out.println("word length group : "+words.stream().collect(Collectors.groupingBy(String::length)));
        // {4=java, 5=hello, world, 7=streams, 10=collecting}
        System.out.println(words.stream().collect(Collectors.groupingBy(String::length, Collectors.joining(", "))));
        // {4=1, 5=2, 7=1, 10=1}
        System.out.println(words.stream().collect(Collectors.groupingBy(String::length, Collectors.counting())));
        TreeMap<Integer, Long> treeMap = words.stream().collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.counting()));
        // treeMap : {4=1, 5=2, 7=1, 10=1}
        System.out.println("treeMap : "+treeMap);

        // 8. Partitioning Elements - {false=[hello, world, java], true=[streams, collecting]}
        //  Partitions elements into two groups (true and false) based on a predicate
        System.out.println(words.stream().collect(Collectors.partitioningBy(x -> x.length() > 5)));

        // 9. Mapping and Collecting - [HELLO, WORLD, JAVA, STREAMS, COLLECTING]
        // Applies a mapping function before collecting
        System.out.println(words.stream().collect(Collectors.mapping(x -> x.toUpperCase(), Collectors.toList())));

        // 10. toMap

        // Collecting Names by Length - {3=[Bob], 4=[Anna], 5=[Brian, Alice], 9=[Alexander]}
        List<String> l1 = Arrays.asList("Anna", "Bob", "Alexander", "Brian", "Alice");
        System.out.println(l1.stream().collect(Collectors.groupingBy(String::length)));

        // Counting Word Occurrences - {java=1, world=2, hello=2}
        String sentence = "hello world hello java world";
        System.out.println(Arrays.stream(sentence.split(" ")).collect(Collectors.groupingBy(x -> x, Collectors.counting())));

        // Partitioning Even and Odd Numbers - {false=[1, 3, 5], true=[2, 4, 6]}
        List<Integer> l2 = Arrays.asList(1, 2, 3, 4, 5, 6);
        System.out.println(l2.stream().collect(Collectors.partitioningBy(x -> x % 2 == 0)));

        // Summing Values in a Map - 10+20+15 = 45
        Map<String, Integer> items = new HashMap<>();
        items.put("Apple", 10);
        items.put("Banana", 20);
        items.put("Orange", 15);
        System.out.println(items.values().stream().reduce(Integer::sum)); // Optional[45]
        System.out.println(items.values().stream().collect(Collectors.summingInt(x -> x))); // 45

        // Creating a Map from Stream Elements - {CHERRY=6, APPLE=5, BANANA=6}
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
        System.out.println(fruits.stream().collect(Collectors.toMap(x -> x.toUpperCase(), x -> x.length())));

        // key - unique item from list, value - total count of key - {orange=1, banana=2, apple=3}
        List<String> words2 = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple");
        System.out.println(words2.stream().collect(Collectors.toMap(k -> k, v -> 1, (x, y) -> x + y)));;

    }
}
