// package com.java.code.fileprocessor;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.channels.FileLock;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class ReadFile {
    protected static String ReadCSV(String filePath) throws IOException {
        StringBuilder data = new StringBuilder();
        FileInputStream fis = null;
        FileChannel channelCSV = null;
        FileLock lockCSV = null;
        BufferedReader br = null;

        try {
            fis = new FileInputStream(filePath);
            channelCSV = fis.getChannel();
            lockCSV = channelCSV.lock(0L, Long.MAX_VALUE, true); // Shared lock

            if (lockCSV != null) {
                br = new BufferedReader(new InputStreamReader(fis));
                List<String[]> csvData = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    csvData.add(values);
                }

                for (String[] row : csvData) {
                    data.append(Arrays.toString(row)).append("\n");
                }
            } else {
                System.err.println("Could not acquire lock on file: " + filePath);
            }
        } finally {
            if (lockCSV != null) {
                lockCSV.release();
            }
            if (channelCSV != null) {
                channelCSV.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return data.toString();
    }
}


class WriteFile{
    protected static boolean WriteText(String filePath,String data)throws IOException{
        try(FileOutputStream fos = new FileOutputStream(filePath)){
            FileChannel writeChannel = fos.getChannel();
            FileLock writeLock = writeChannel.lock();
            while(writeLock!=null){
                fos.write(data.getBytes());
                writeLock.release();
            }
            return true;
        }catch(IOException e){
            System.err.println("Error in WriteFile() "+e.getMessage());
            //throw e;        // handle exception
            return false;
        }
    }

}

class ReadWriteTask implements Runnable {
    private String inputFilePath;
    private String outputFilePath;

    public ReadWriteTask(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void run() {
        String fileData = "";
        try {
            if (inputFilePath.toLowerCase().endsWith(".txt")) {
                System.out.println("Reading ..... "+inputFilePath);
                fileData = ReadFile.ReadText(inputFilePath);
            } else if (inputFilePath.toLowerCase().endsWith(".csv")) {
                fileData = ReadFile.ReadCSV(inputFilePath);
                System.out.println("Reading ..... "+inputFilePath);
            }
            System.out.println("Writing ..... "+outputFilePath);
            WriteFile.WriteText(outputFilePath, fileData);
        } catch (IOException e) {
            System.err.println("Error processing file: " + inputFilePath + " - " + e.getMessage());
        }
    }
}

public class ThreadPoolFileProcess{
    public static void main(String[] args)throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        final String outputName = "copy-of.txt";
        File folder = new File("."); 

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                String filePath = file.getAbsolutePath();
                executor.submit(new ReadWriteTask(filePath, outputName));
            }
        }

        executor.shutdown();
    }
}

// protected static String ReadCSV(File filePath)throws IOException{
    //     String data;
    //     List<String[]> csvData = new ArrayList<>();
    //     try(BufferedReader br = new BufferedReader(new FileReader(filePath.toPath()))){
    //         String line;
    //         while((line = br.readLine())!=null){
    //             String[] values = line.split(",");
    //             csvData.add(values);
    //         }
    //         for (String[] row: csvData) {
    //             data = data.concat(Arrays.toString(row) +"\n");
    //         }
    //         return data;
    //     }catch(IOexception e){
    //         System.err.println("Error in ReadCSV() "+e.getMessage());
    //         return data;
    //     }

    // }


// protected static String ReadText(String filePath)throws IOException{
//         String data="";
//         try(FileInputStream fis = new FileInputStream(filePath)){
//             FileChannel channelText = fis.getChannel();
//             FileLock lockText = channelText.tryLock(0L,Long.MAX_VALUE,true);
//             while(lockText!=null){
//                 byte[] buffer = new byte[fis.available()];
//                 fis.read(buffer);
//                 data = new String(buffer);
//                 readLock.release();
//             }
//         }catch(IOexception e){
//             System.err.println("Error in ReadText() "+e.getMessage());
//         }
//         return data;
//     }