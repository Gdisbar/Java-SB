 


Encapsulation vs Abstraction 
Java Keywords - Public,Private,Protected,Default
throw vs throws
static - block,method,attributes
final vs finally
constructor - default,parameterized,copy 
polymorphism - definition 
inheritance - definition (go with example) , super
composition


Reading a File with Memory Mapping - 

FileChannel channel = file.getChannel()) {
	// Map the file directly into memory
	MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
	// Read data from memory
	while (buffer.hasRemaining()) {
	    System.out.print((char) buffer.get());
	}

what happens if b






Explain me these topics under OOPs in details, consider I'm want to prepare for Java  developer role that require atleast 3 years hands on industrial experience so include contents keeping in mind that fact also provide as much industrial example as possible


Give me 10 Java coding practice problem that can be asked in interview for a 5 years
experienced developer on the topic of Threading & Exception (it might cover other topics
in core/advanced java). The questions should test my skills in using Java for industry
standard not just some practice question,avoid very long questions (like those LLD questions that takes long time to complete)

avoid questions complex Data structure & algorithms questions & questions from these advanced java topics

JDBC
Connection Pools - Hikari CP, Apache CP
Transaction Management
Isolation & Propagation Strategies
Generics
Stateful & Stateless Beans
URIs & Protocols)


Give me 10 Java coding practice problem that can be asked in interview for a 3 years
experienced developer on the topic of OOP (it might cover other topics
in core/advanced java). The questions should test my skills in using Java for industry
standard not just some practice question,avoid very long questions (like those LLD questions that takes long time to complete)

avoid questions complex Data structure & algorithms questions & questions from these advanced java topics

JDBC
Connection Pools - Hikari CP, Apache CP
Transaction Management
Isolation & Propagation Strategies
Generics
Stateful & Stateless Beans
URIs & Protocols)


AutoBoxing

File handling
------------------------------------------------
BufferedReader --> line level access,slow
InputStream ---> File level access,fast

Format/Encoding --> Microsoft (Apache),Normal: handled by Java itself


Rendering data inside Java program ---> use java class
Loading outside data into Java program ----> can use byte[] if needed

mvn dependency:resolve
mvn dependency:list

/home/acro0nix/Programs/Java_Codes/src/main/java/com/javacode/fileprocessor/ThreadPoolFileProcess.java


mv /home/acro0nix/Programs/Java_Codes/src/main/java/pom.xml /home/acro0nix/Programs/Java_Codes/pom.xml

cd /home/acro0nix/Programs/Java_Codes
mvn clean compile
mvn exec:java -Dexec.mainClass="com.javacode.fileprocessor.ThreadPoolFileProcess"


Limit CPU	 -->  cpulimit --limit 30 java -jar app.jar
Limit Memory (JVM)	--> java -Xmx512m -Xms256m -jar app.jar
Limit Memory (Linux) --->	cgexec -g memory,cpu:java_limit java -jar app.jar
Lower CPU Priority  --->	nice -n 19 java -jar app.jar
Lower IO Priority	---> ionice -c3 -p <PID>