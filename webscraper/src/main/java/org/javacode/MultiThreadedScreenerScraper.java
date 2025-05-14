package org.javacode;


import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiThreadedScreenerScraper {

    public static void main(String[] args) {
        List<String> tickers = Arrays.asList("HDFCBANK", "EICHERMOT", "GRASIM", "DRREDDY", "BRITANNIA");

        ExecutorService executor = Executors.newFixedThreadPool(tickers.size());
        ScreenerScraper scraper = new ScreenerScraper();

        // Futures of TickerData type
        List<Future<ScreenerScraper.TickerData>> futures = new ArrayList<>();

        for (String ticker : tickers) {
            System.out.println("Fetching job for " + ticker + "submitted to executor");
            futures.add(executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    return scraper.scrapScreener(ticker);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread for ticker " + ticker + " was interrupted: " + e.getMessage());
                    return new ScreenerScraper.TickerData(ticker);
                } catch (Exception e) {
                    System.err.println("Error processing ticker " + ticker + ": " + e.getMessage());
                    return new ScreenerScraper.TickerData(ticker);
                }
            }));
        }

        executor.shutdown();

        // Result: HashMap<ticker, Hashmap<report,data>>
        // data : HashMap<attribute,List<value>>
        HashMap<String, HashMap<String, HashMap<String, List<String>>>> finalResult = new HashMap<>();

        for (Future<ScreenerScraper.TickerData> future : futures) {
            try {
                ScreenerScraper.TickerData tickerData = future.get();
                finalResult.put(tickerData.getTicker(), tickerData.getData());
            } catch (InterruptedException | ExecutionException e) {
                // e.printStackTrace();
                System.err.println("Exception in "+Thread.currentThread().getStackTrace()[1].getMethodName());
            }
        }

        // Convert finalResult to JSON and write to a file
        JSONObject json = new JSONObject(finalResult);

        try (FileWriter file = new FileWriter("src/main/resources/scrapped_data.json")) {
            file.write(json.toString(4)); // Pretty print JSON
            System.out.println("Successfully wrote data to src/main/resources/scrapped_data.json");
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("Exception in FileWriter");
        }
    }
}