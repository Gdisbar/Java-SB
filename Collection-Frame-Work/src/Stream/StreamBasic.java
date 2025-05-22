package Stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
What is stream ?
a sequence of elements supporting functional and declarative programing

How to Use Streams ?
Source, intermediate operations & terminal operation

// we need to create separate variable for separate stream operation
Since we can't operate on Streams once terminal operation is performed
we can only run any one of the below

// Creating Streams

// 1. From collections
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
Stream<Integer> stream1 = list.stream();

// 2. From Arrays
String[] array = {"a", "b", "c"};
Stream<String> stream2 = Arrays.stream(array);

// 3. Using Stream.of()
Stream<String> stream3 = Stream.of("a", "b");

// 4. Infinite streams
// Stream.generate(() -> Integer.valueOf(1));
// Stream.iterate(1, x -> Integer.valueOf(x + 1));

// For primitive type use any of the below 3 options
int[] numbers = {1, 2, 3, 4, 5};
IntStream stream4 = Arrays.stream(numbers).boxed()
IntStream stream4 = (Stream<Integer>)Arrays.stream(numbers)
IntStream stream4 = Arrays.stream(numbers);
System.out.println(IntStream.range(1, 5).boxed().collect(Collectors.toList()));

Intermediate operations transform a stream into another stream
They are lazy, meaning they don't execute until a terminal operation is invoked.

// 7. flatMap
Handle streams of collections, lists, or arrays where each element is itself a collection
Flatten nested structures (e.g., lists within lists) so that they can be processed as a
single sequence of elements
Transform and flatten elements at the same time.

* */

public class StreamBasic {
    public static void main(String[] args) {

        System.out.println(IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList()));
        IntStream intStream = IntStream.of(1, 2, -3, -4, 5, -6); // IntPipeline$Head@24d46ca6
        System.out.println(intStream.boxed().toList());

        // Only one terminal operation
        DoubleStream doubles = new Random().doubles(5);
        try{
            IntStream mappedIntStream = doubles.mapToInt(x -> (int) (x + 1));
            List<Integer> intList =mappedIntStream.boxed().toList();
            System.out.println(intList); // [1, 1, 1, 1, 1]
        }catch (IllegalStateException ignore){
            System.out.println("stream has already been operated upon or closed : "
                    + ignore.getCause());
        }
        // DoubleSummaryStatistics doublesStatistics = doubles.summaryStatistics();

        // To run both
        double[] randomValues = new Random().doubles(5).toArray();
        DoubleSummaryStatistics stats = Arrays.stream(randomValues).summaryStatistics();
        System.out.println(stats.getCount() + " " + stats.toString());

        // 1. filter
        List<String> list1 = Arrays.asList("Akshit", "Ram", "Shyam", "Ghanshyam", "Akshit");
        Stream<String> filteredStream = list1.stream().filter(x -> x.startsWith("A"));
        // no filtering at this point
        long res = list1.stream().filter(x -> x.startsWith("A")).count();
        System.out.println(res); // 2
        // 2.map - [AKSHIT, RAM, SHYAM, GHANSHYAM, AKSHIT]
        Stream<String> stringStream = list1.stream().map(String::toUpperCase);
        System.out.println("stringStream : "+ stringStream.toList());
        // 3. sorted reverse order - [Ram, Shyam, Akshit, Akshit, Ghanshyam]
        Stream<String> sortedStreamUsingComparator = list1.stream().sorted((a, b) -> a.length() - b.length());
        System.out.println("sortedStreamUsingComparator : "+sortedStreamUsingComparator.toList());
        // 4. distinct - 1 [Akshit]
        System.out.println("distinct : "+list1.stream().filter(x -> x.startsWith("A")).distinct().count());
        // 5. limit + skip - 10
        System.out.println("limit + skip : "+Stream.iterate(1, x -> x + 1).skip(5).limit(10).count());
        // 6. peek - [4+1,5+1,...,13+1] : total 10
        // Performs an action on each element as it is consumed.
        Stream.iterate(1, x -> x + 1).skip(4).limit(10).peek(System.out::println).count();

        // 7. flatMap
        List<List<String>> listOfLists = Arrays.asList(
                Arrays.asList("apple", "banana"),
                Arrays.asList("orange", "kiwi"),
                Arrays.asList("pear", "grape")
        );
        System.out.println(listOfLists.get(1).get(1)); // kiwi
        // [APPLE, BANANA, ORANGE, KIWI, PEAR, GRAPE]
        System.out.println(listOfLists.stream().flatMap(x -> x.stream()).map(String::toUpperCase).toList());
        List<String> sentences = Arrays.asList(
                "Hello world",
                "Java streams are powerful",
                "flatMap is useful"
        );
        // split+ sort - [HELLO, WORLD, JAVA, STREAMS, ARE, POWERFUL, FLATMAP, IS, USEFUL]
        System.out.println("split + sort : " + sentences
                .stream()
                .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
                .map(String::toUpperCase)
                .toList());

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        Stream<String> stream5 = names.stream()
                .filter(name -> {
                    System.out.print ("Filtering: " + name + " , ");
                    return name.length() > 3;
                });
        // java.util.stream.ReferencePipeline$2@5b6f7412
        //Filtering: Alice , Filtering: Bob , Filtering: Charlie , Filtering: David
        System.out.println("Before Terminal Ops : \n"+stream5);
        // [Alice, Charlie, David]
        System.out.println("\nAfter Terminal Ops : "+stream5.collect(Collectors.toList()));

        // 1. collect
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
//        list.stream().skip(1).collect(Collectors.toList());
//        list.stream().skip(1).toList();

        // 2. forEach
        list.stream().forEach(x -> System.out.println(x));

        // 3. reduce : Combines elements to produce a single result - 1+2+3+4+5 = 15
        Optional<Integer> optionalInteger = list.stream().reduce(Integer::sum);
        System.out.println(optionalInteger.get());

        // 4. count

        // 5. anyMatch, allMatch, noneMatch
        System.out.println(list.stream().anyMatch(x -> x % 2 == 0)); // true
        System.out.println(list.stream().allMatch(x -> x > 0)); // true
        System.out.println(list.stream().noneMatch(x -> x < 0)); // true

        // 6. findFirst, findAny
        System.out.println(list.stream().findFirst().get()); // 1
        System.out.println(list.stream().findAny().get()); // 1
        // 7. toArray() - [Ljava.lang.Object;@4dd8dc3
        Object[] array = Stream.of(1, 2, 3).toArray();
        System.out.println(Arrays.stream(array).toList()); //[1, 2, 3]
        // 8. min / max - both showing min due to implementation : Optional[2]
        System.out.println("max: " + Stream.of(2, 44, 69).max((o1, o2) -> o2 - o1));
        System.out.println("min: " + Stream.of(2, 44, 69).min(Comparator.naturalOrder()));

        // 9. forEachOrdered
        List<Integer> numbers0 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("Using forEach with parallel stream:");
        numbers0.parallelStream().forEach(System.out::println); // [7,6,9,...2] : not orderly
        System.out.println("Using forEachOrdered with parallel stream:");
        numbers0.parallelStream().forEachOrdered(System.out::println); // [1,2,3,...,10] : orderly

        // Filtering and Collecting Names - [Alice, Charlie, David]
        System.out.println(names.stream().filter(x -> x.length() > 3).toList());
        // Squaring and Sorting Numbers - [1, 4, 9, 16, 25]
        System.out.println(list.stream().map(x -> x * x).sorted().toList());
        // Counting Occurrences of a Character
        String sentence = "Hello world";
        System.out.println(sentence.chars().filter(x -> x == 'l').count()); // 3

    }
}
