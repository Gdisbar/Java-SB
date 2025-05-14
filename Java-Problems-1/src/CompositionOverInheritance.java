import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import java.io.*;

class LoggerComponent{
	protected void log(String message){
		throw new UnsupportedOperationException("subclass must implement log()");
	}
}
class FileLogComponent extends LoggerComponent{

	protected void log(String message)throws UnsupportedOperationException{
		try(FileOutputStream fos = new FileOutputStream("example-3.txt",true)){
			// make these changes
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos));
				String data = "This is the log for FileLogComponent class message : "+message;
				FileChannel writeChannel = fos.getChannel();
				FileLock writeLock = writeChannel.lock();
				if(writeLock!=null){
					buffer.write(data);
					buffer.newLine(); 
					buffer.flush(); // Force the buffer to write to the file
					writeLock.release();
				}
		}catch(IOException e){
			System.err.println("IOException in FileLogComponent: " + e.getMessage());
		}
	}
}

class DatabaseLogComponent extends LoggerComponent {
	// @Override // no need for this 
    protected void log(String message)throws UnsupportedOperationException {
        try(FileOutputStream fos = new FileOutputStream("example-3.txt",true)){
            // make these changes
            BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos));
                String data = "This is the log for DatabaseLogComponent class message : "+message;
                FileChannel writeChannel = fos.getChannel();
                FileLock writeLock = writeChannel.lock();
                if(writeLock!=null){
                    buffer.write(data);
                    buffer.newLine(); 
                    buffer.flush(); // Force the buffer to write to the file
                    writeLock.release();
                }
        }catch(IOException e){
            System.err.println("IOException in DatabaseLogComponent: " + e.getMessage());
        }
    }
}


class FormatterComponent{
	protected String format(String message){
		throw new UnsupportedOperationException("subclass must implement format()");
	}
}

class HTMLFormatterComponent extends FormatterComponent{

	protected String format(String message)throws UnsupportedOperationException{
		try{
			return "<html><body>{message}</body></html>";
		}catch(UnsupportedOperationException e){
			return "UnsupportedOperationException in HTMLFormatterComponent: " + e.getMessage();
		}
	}
}

class UppercaseFormatterComponent extends FormatterComponent {
    public String format(String message)throws UnsupportedOperationException {
        try{
            return message.toUpperCase();
        }catch(UnsupportedOperationException e){
            return "UnsupportedOperationException in UppercaseFormatterComponentt: " + e.getMessage();
        }
    }
}

class Application{
	private LoggerComponent loggerComponent;
	private FormatterComponent formatterComponent;
	protected  Application(){};
	protected Application(LoggerComponent loggerComponent,FormatterComponent formatterComponent){
		this.loggerComponent = loggerComponent;
		this.formatterComponent = formatterComponent;
	}
	protected void processData(String data)throws UnsupportedOperationException{
		try{
			formatterComponent.format(data);
			loggerComponent.log(data);
		}catch(UnsupportedOperationException e){
			System.err.println("UnsupportedOperationException occured in processData()"+e.getMessage());
		}
		
	}
}

public class CompositionOverInheritance{
	public static void main(String[] args)throws UnsupportedOperationException {
		try{
			FileLogComponent fileLog = new FileLogComponent();
			HTMLFormatterComponent htmlFormatter = new HTMLFormatterComponent();
			Application app1 = new Application(fileLog,htmlFormatter);
			app1.processData("This is Composition - FileLogComponent+HTMLFormatter\n");
		}catch(UnsupportedOperationException e){
			System.err.println("UnsupportedOperationException occured in app1-main()"+e.getMessage());
		}

        try{
            DatabaseLogComponent databaseLog = new DatabaseLogComponent();
            UppercaseFormatterComponent uppercaseFormatter = new UppercaseFormatterComponent();
            Application app2 = new Application(databaseLog, uppercaseFormatter);
            app2.processData("This is Composition - DatabaseLogComponent + UppercaseFormatterComponent\n");
        }catch(UnsupportedOperationException e){
            System.err.println("UnsupportedOperationException occured in app2-main()"+e.getMessage());
        }

        try{
            DatabaseLogComponent databaseLog = new DatabaseLogComponent();
            HTMLFormatterComponent htmlFormatter = new HTMLFormatterComponent();
            Application app3 = new Application(databaseLog, htmlFormatter);
            app3.processData("This is Composition - DatabaseLogComponent + HTMLFormatter\n");
        }catch(UnsupportedOperationException e){
            System.err.println("UnsupportedOperationException occured in app3-main()"+e.getMessage());
        }
	}
}

// import java.io.*;

// // Inheritance Example (Problematic)

// class Logger {
//     public void log(String message) {
//         System.out.println("Logging: " + message);
//     }
// }

// class FileLogger extends Logger {
//     @Override
//     public void log(String message) {
//         try (FileWriter fw = new FileWriter("log.txt", true);
//              BufferedWriter bw = new BufferedWriter(fw)) {
//             bw.write(message + "\n");
//         } catch (IOException e) {
//             System.err.println("Error writing to file: " + e.getMessage());
//         }
//     }
// }

// class DatabaseLogger extends Logger {
//     @Override
//     public void log(String message) {
//         // Simulate database logging
//         System.out.println("Database Log: " + message);
//     }
// }

// class Formatter {
//     public String format(String message) {
//         return message;
//     }
// }

// class UppercaseFormatter extends Formatter {
//     @Override
//     public String format(String message) {
//         return message.toUpperCase();
//     }
// }

// class HTMLFormatter extends Formatter {
//     @Override
//     public String format(String message) {
//         return "<html><body>" + message + "</body></html>";
//     }
// }

// class App {
//     private Logger logger;
//     private Formatter formatter;

//     public App(Logger logger, Formatter formatter) {
//         this.logger = logger;
//         this.formatter = formatter;
//     }

//     public void processData(String data) {
//         String formattedData = formatter.format(data);
//         logger.log(formattedData);
//     }
// }

// public class CompositionOverInheritance {
//     public static void main(String[] args) {
//         FileLogger fileLogger = new FileLogger();
//         UppercaseFormatter uppercaseFormatter = new UppercaseFormatter();

//         App app = new App(fileLogger, uppercaseFormatter);
//         app.processData("Hello, world!");

//         // Problem: What if we want DatabaseLogger with HTMLFormatter?
//         // We'd need to create a new class, which is not ideal.
//     }
// }
