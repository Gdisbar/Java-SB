package fileprocessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;


interface DocumentProcessor{
    public MappedByteBuffer LoadFile(String filePath)throws Exception;
    void ParseFile(String filePath, String targetFile, int chunkSize)throws Exception;
    public boolean RenderFile(String data);
    public void SaveFile(String data,String targetFile)throws Exception;
}

class TextProcessor implements DocumentProcessor{
    public MappedByteBuffer LoadFile(String filePath)throws Exception{
        try(RandomAccessFile rf = new RandomAccessFile(filePath,"rw") ){
            FileChannel channel = rf.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE,0,channel.size());
            return buffer;
        }catch(Exception e){
            System.out.println("Error in Loadfile(filePath)");
            throw e;
        }
    }
    public void ParseFile(String filePath,String targetFile,int chunkSize) throws Exception{ // Extract Data
        try{
            MappedByteBuffer buffer = LoadFile(filePath);
            int chunkNumber = 0;
            byte[] chunk = new byte[chunkSize];
            while(buffer.hasRemaining()){
                int bytesToRead = Math.min(chunkSize, buffer.remaining());
                buffer.get(chunk, 0, bytesToRead); // Read chunk of bytes
                String data = new String(chunk, 0, bytesToRead); // Convert to String
                chunkNumber += 1;
                System.out.println("Processing Chunk : "+ chunkNumber);
                try{
                    RenderFile(data);
                    System.out.println("Processed Chunk : "+ chunkNumber);
                }catch(Exception e){
                    System.out.println("Eror in processing RenderFile(data)");
                }
                try{
                    SaveFile(data,targetFile);
                    System.out.println("Saved data in SaveFile(data,targetFile)");
                }catch(Exception e){
                    System.out.println("Error in processing SaveFile(data,targetFile)");
                }

            }
        }catch(Exception e){
            System.out.println("Error in ParseFile(filePath,targetFile)");
        }
    }
    public boolean RenderFile(String data){
        if(data.length()>0){
            System.out.println("Length of data chunk "+data.length()+" data : "+data.substring(0,10));
            return true;
        }else{
            System.out.println("Failed to get the data...Length of data chunk "+data.length());
            return false;
        }
    }
    public void SaveFile(String data,String targetFile)throws Exception{
        if(data.length()>0){
            File fp = new File(targetFile);
            if (fp.exists()){
                System.out.println("File Saved");
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile,true))){
                    bw.write(data); // true in FileWriter() append in existing file,no re-write
                }catch(Exception e){
                    System.out.println("Error in Saving SaveFile(data,targetFile)");
                    throw e;
                }
            }
        }
    }

}



class SimpleFileProcessor{
    public static void main(String[] args) throws Exception{
        try{
            TextProcessor dp = new TextProcessor();
            int chunkSize=4;
            dp.ParseFile("src/fileprocessor/resources/example-2.txt","src/fileprocessor/resources/example-2.txt",chunkSize);
        }catch(Exception e){
            System.out.println("Exception in "+e.getClass());
        }

    }
}
