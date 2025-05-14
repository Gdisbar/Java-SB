
Object of java.lang.String
---------------------------------------------------------
The String class is final → Cannot be subclassed.
It implements the Serializable, Comparable, and CharSequence interfaces.
Since String implements Comparable, strings can be compared using compareTo() method for sorting and ordering.
In Java 7+, the String Pool was moved from PermGen to the main heap area.
Internally, strings are represented as a char array (Java 8 and earlier) or a byte array (Java 9+) for memory efficiency.

Memory Efficient – JVM maintains a String Pool to store strings efficiently.
Any operation that appears to modify a string actually creates a new String object. 
This is why string references in the pool remain safe.
Immutable strings enable JVM optimizations such as caching and reusing string objects
any operation that modifies a string creates a new String object.

Fast – Allows safe use in multithreaded environments.
safely shared across threads without synchronization.

String str = "Hello";
str.concat(" World"); // new string is "Hello World" but we don't have the object for it
System.out.println(str);  // Output: Hello , since String is immutable

At Compile Time
-------------------------------------------------------------------------
When the compiler encounters a string literal, it checks the String Pool.
If the string exists → It returns the existing reference.
If the string doesn't exist → It creates a new string object and places it in the pool.

At Runtime
-------------------------------------------------------------------------
If new String() is used, a new object is created in the Heap, bypassing the pool.
If intern() is called, the string is added to the pool (if not already present).

String s1 = new String("Java");
String s2 = s1.intern(); // Places "Java" in the pool
System.out.println(s1 == s2); // false → Different objects
System.out.println(s2 == "Java"); // true → Same object in the pool

Creating String
--------------------------------
String Literal -> String Pool -> Reuses existing string if already present
new Keyword -> Heap Memory -> 	Always creates a new object

String methods
----------------------------------------
String str = "apple,orange,banana";
String[] fruits = str.split(",");
for (String fruit : fruits) {
    System.out.println(fruit);
}

String formatted = String.format("Value: %d", 42);

throws StringIndexOutOfBoundsException -> charAt(index),substring(start, end) // out of index
indexOf(substring) -> -1 if not exist

equals() – Compares the actual content. // s1.equals(s2),s1.equalsIgnoreCase(s2)
== – Compares the reference. // s1 == s2

The + operator is optimized by the compiler using StringBuilder when concatenating multiple strings, especially in loops
concat() does create a new String object, but so does the + operator ultimately

0 – if equal
<0 – if first string (lexicographically) is smaller (precedes)
>0 – if first string (lexicographically) is larger (follows)

String s1 = "Apple";
String s2 = "Banana";
System.out.println(s1.compareTo(s2)); // Output: -1

Empty vs null: Empty string ("") is different from null - empty has a reference but no characters, null has no reference at all.

StringBuffer – Thread-safe but slower 
--------------------------------------------------------
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World");

StringBuilder – Not thread-safe but faster
----------------------------------------------------------------
StringBuilder sb = new StringBuilder("Java");
sb.append(" Rocks");

Conversion
-----------------------------------------------------------------
int num = Integer.parseInt("123");
String str = String.valueOf(456);
char[] chars = "Hello".toCharArray();
byte[] bytes = "Hello".getBytes();

Best Practices for Efficient String Handling
--------------------------------------------------------------------------------
✅ Use StringBuilder for dynamic string manipulation
✅ Use intern() to reduce memory overhead
✅ Avoid creating strings using new String() unless necessary
✅ Use StringBuffer only when thread safety is required
✅ Avoid using + operator in loops
✅ Use String.join() (Java 8+) for joining strings