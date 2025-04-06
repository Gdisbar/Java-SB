java.io
├── InputStream (abstract)
│   ├── FileInputStream
│   ├── ByteArrayInputStream
│   └── ObjectInputStream
├── OutputStream (abstract)
│   ├── FileOutputStream
│   ├── ByteArrayOutputStream
│   └── ObjectOutputStream
├── Reader (abstract)
│   ├── FileReader
│   ├── BufferedReader
├── Writer (abstract)
    ├── FileWriter
    ├── BufferedWriter

Byte Streams → InputStream, OutputStream : Used for binary data (images, videos, audio)
Character Streams → Reader, Writer : Used for text data (files, strings)

✅ File Class
---------------------------------------------------------------------------------------
java.io.File is used to represent a file or directory path.
It does NOT handle the actual content of the file — it only represents the file metadata. 

✅ Important Methods in File Class 
| Method | Description |
|--------|-------------|
| delete() | Deletes the file |
| exists() | Checks if the file exists |
| getAbsolutePath()` | Returns absolute path |
| length() | Returns file size |
| canRead() | Checks read permission |
| canWrite() | Checks write permission |

File file = new File("example.txt");
if (file.createNewFile()) {
    System.out.println("File created: " + file.getName());
}else{}

File myDirectory = new File("myDirectory");
if (myDirectory.mkdir()) {
    System.out.println("Directory created: " + myDirectory.getName());
} else {//code//}

// List files in a directory
File directoryToList = new File("."); // Current directory
String[] files = directoryToList.list();
if (files != null) {
    System.out.println("Files in directory:");
    for (String file : files) {
        System.out.println(file);
    }
}

✅ FileInputStream,FileOutputStream
---------------------------------------------------------------
- Read/Writes raw byte data -> images and binary files  

✅ Character Streams 
- Works with 16-bit Unicode characters (reading and writing text files.)

✅ FileReader & FileWriter
-------------------------------------------------------------------------------
- Reads/Writes characters  

🔥 Read/Write using FileReader,FileWriter
-------------------------------------------------------------------
same as FileInputStream,FileOutputStream but no need to convert to Bytes data.getBytes()


✅ Buffered Streams
----------------------------------------------------------------------------------------
- BufferedReader and BufferedWriter provide buffering for efficient I/O operations.  
- Reduces disk I/O operations by working with a buffer in memory.  


🔥 Read from a large File using BufferedReader
------------------------------------------------------------------------------
try(BufferedReader br = new BufferedReader(new FileReader("example.txt"))){ 
    String line;
    String data = "";
    while((line=br.readLine())!=null){
        data = data.concat(line+"\n");
    }
    try(BufferedWriter bw = new BufferedWriter(new FileWriter("example-2.txt",true))){
        bw.write(data); // true in FileWriter() append in existing file,no re-write
    }catch(IOException e){ e.printStackTrace();}
}catch(IOException e){e.printStackTrace();}

🔥 Chunked Processing for Large Files
------------------------------------------------------------------------------
try(RandomAccessFile rf = new RandomAccessFile("example-2.txt","r")){
    byte[] buffer = new byte[512];
    int byteread;
    while((byteread=rf.read(buffer))!=-1){
        System.out.println(new String(buffer,0,byteread));
    }
}catch(IOException e){e.printStackTrace();}

✅ Object Streams 
----------------------------------------------------------------------------------------
- ObjectInputStream and ObjectOutputStream are used to read and write objects.  
- Objects must implement the Serializable interface.  

🔥 Write Object using ObjectOutputStream
-------------------------------------------------------------------------
class Person implements Serializable {
    String name;
    int age;
}

try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.obj"))) {
    Person p = new Person();
    p.name = "John";
    p.age = 30;
    oos.writeObject(p);
    System.out.println("Object written to file");
} catch (IOException e) {
    e.printStackTrace();
}

✅ Best Practices
✅ Always close streams using try-with-resources.  
✅ Use BufferedReader and BufferedWriter for better performance.  
✅ Use ObjectOutputStream for writing objects.  
✅ Avoid FileOutputStream for large files — use BufferedOutputStream instead.  
✅ Handle exceptions properly — avoid silent failure.  


✅ File Locking 
-------------------------------------------------------------------------------------
- File locking ensures that only one process/thread can access a file at a time. 
- It helps prevent data corruption and ensures consistency in concurrent environments.
- Java provides FileChannel and FileLock classes from the java.nio.channels package to 
handle file locking.



🔎 Types of File Locks
1. Shared Lock → Multiple processes can read simultaneously (but not write).  
2. Exclusive Lock → Only one process can access the file for reading and writing.  

FileChannel - more efficient and flexible file I/O operations compared to byte-by-byte reading/writing.

- FileChannel.lock() creates an exclusive lock.  
- FileChannel.tryLock(0L, Long.MAX_VALUE, true) creates a shared lock. 

🔥 Exclusive File Locking
-------------------------------------------
try(FileOutputStream fos = new FileOutputStream("example-2.txt")){
    FileChannel writechannel = fos.getChannel();
    FileLock writelock = writechannel.lock();
    String data = "Hello world\n";
    if(writelock!=null){
        fos.write(data.getBytes());
        writelock.release();
    }
    }catch(IOException e){e.printStackTrace();}


🔥 Shared File Locking
------------------------------------------- 
try(FileInputStream fis = new FileInputStream("example.txt")){
// int i;
// while((i=fis.read())!=-1){
//     System.out.print((char) i);
// }
FileChannel readchannel = fis.getChannel();
FileLock readlock = readchannel.tryLock(0L, Long.MAX_VALUE, true);
if (readlock!=null){
    byte[] data = new byte[fis.available()];
    fis.read(data);
    System.out.println(new String(data));
    readlock.release();
}


✅ Best Practices
✅ Use shared locks for read operations and exclusive locks for write operations.  
✅ Always release the lock to prevent deadlocks.  
✅ Avoid holding locks for long periods — they are OS-level resources.  



✅ Memory-Mapped Files
------------------------------------------------------------------------------------
Memory-mapped files allow Java to map a file directly into memory for faster reading and writing.  
- Uses the `java.nio.MappedByteBuffer` and `java.nio.channels.FileChannel` classes.  
- OS-level memory mapping ensures low-latency I/O.  
- Ideal for very large files (GBs) since data is loaded in chunks instead of reading the whole file.  


✅ How Memory Mapping Works  
1. The file is loaded directly into the process address space.  
2. No need to copy data from kernel space to user space → High-performance I/O.  
3. File changes are directly reflected in memory.  

- MappedByteBuffer creates a direct buffer in the native heap.  
- OS directly maps the file to process memory — no user/kernel space transfer.  


// 🔥(Nive jaa) Reading a File with Memory Mapping
// ------------------------------------------------------------------
// try (RandomAccessFile file = new RandomAccessFile("example.txt", "rw");
//      FileChannel channel = file.getChannel()) {
//     // Map the file directly into memory
//     MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
//     // Read data from memory
//     while (buffer.hasRemaining()) {
//         System.out.print((char) buffer.get());
//     }

//     // Write to the file via memory-mapped buffer
//     buffer.put(0, (byte) 'H'); // Modify first byte directly in memory
//     buffer.force(); // Force changes to be written to disk.
//     System.out.println("\nFile modified");

// } catch (Exception e) {
//     e.printStackTrace();
// }


✅ Best Practices:
✅ Use memory-mapped files for large file processing.  
✅ Avoid memory-mapping very large files — can exhaust JVM heap space.  
✅ Ensure proper permissions when mapping files for writing.  


✅ Techniques to Handle Large Files  
-------------------------------------------------------------------------------------
1. Buffered Streams → Use `BufferedReader` and `BufferedWriter` to minimize disk I/O.  
2. Memory Mapping → Map large files in chunks using `MappedByteBuffer`.  
3. Splitting and Parallel Processing → Split large files and process them using threads.  
4. Chunked Reading/Writing → Read and write files in smaller chunks.  


🔥 **Next Step:** Do you want to explore file compression, encryption, or NIO (non-blocking I/O)? 😎