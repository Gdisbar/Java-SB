package Hash;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class SetBasic {
    public static void main(String[] args) {
        //  Set is a collection that cannot contain duplicate elements
        // faster operations
        // Map --> HashMap, LinkedHashMap, TreeMap, EnumMap
        // Set --> HashSet, LinkedHashSet, TreeSet, EnumSet
        NavigableSet<Integer> set = new TreeSet<>();
        set.add(12);
        set.add(1);
        set.add(1);
        set.add(67);
        System.out.println("NavigableSet : "+set);
        System.out.println("contains(12) : "+set.contains(12));
        System.out.println("remove(67) : "+set.remove(67));
        set.clear();
        System.out.println("isEmpty() : "+set.isEmpty());
        set.stream().forEach(System.out::println);
        System.out.println("=================");
        // for thread safety

        Set<Integer> set1 = new ConcurrentSkipListSet<>();
        set1.add(112);
        set1.add(11);
        set1.addAll(Arrays.asList(123,154,167));
        System.out.println("ConcurrentSkipListSet : "+set1);
        System.out.println("contains(112) : "+set1.contains(112));
        System.out.println("remove(132) don't exist : "+set1.remove(132));
        System.out.println("isEmpty() : "+set1.isEmpty());
        System.out.println("=================");
        // unmodifiable
        Set<Integer> integers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 54, 4323, 545, 4545);
        try {
            integers.add(112);
        }catch (UnsupportedOperationException e){
            System.out.println("Set.of() is unmodifiable");
            e.printStackTrace();
        }
    }
}
