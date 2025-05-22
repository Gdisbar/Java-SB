package Hash;
/*
Elements in a weak hashmap can be reclaimed by the garbage collector if there are no
other strong references to the key object, this makes them useful for caches/lookup storage.

Weak reference are not restricted to these hash tables, you can use WeakReference for
single objects. They are useful to save resource, you can keep a reference to something
but allow it to be collected when nothing else references it. (BTW, a strong reference is a
normal java reference). There are also weak references which tend not to be as readily
collected as soft references(which don't tend to hang about for long after the last strong
reference disappears)
*/

import java.util.Map;
import java.util.WeakHashMap;

class Image {
    private String name;
    public Image(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}

public class WeakHashMapBasic {

    public static void loadCache(Map<String, Image> imageCache) {
        String k1 = new String("img1");
        String k2 = new String("img2");
        imageCache.put(k1, new Image("Image 1"));
        imageCache.put(k2, new Image("Image 2"));
    }

    private static void simulateApplicationRunning() {
        try {
            System.out.println("Simulating application running...");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WeakHashMap<String, Image> imageCache = new WeakHashMap<>();
        loadCache(imageCache);
        System.out.println(imageCache);
        System.gc();
        simulateApplicationRunning();
        System.out.println("Cache after running (some entries may be cleared): " + imageCache);
    }
}

