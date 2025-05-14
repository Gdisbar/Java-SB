package org.javacode;


import org.json.JSONObject;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScreenerScraper {

    private static final List<String> reports = Arrays.asList(
            "profit-loss", "balance-sheet", "cash-flow", "quarters", "shareholding"
    );

    private static final int THREAD_POOL_SIZE = reports.size();

    // Inner class
    public static class TickerData {
        private final String ticker;
        // data : Hashmap<report,HashMap<attribute,List<value>>>
        private HashMap<String, HashMap<String, List<String>>> data = new HashMap<>();

        public TickerData(String ticker) {
            this.ticker = ticker;
        }

        public void setTickerData(String report) {
            // HashMap<attribute,List<value>>
            HashMap<String, List<String>> result = ScreenerScraperUtil.scrapDataUtil(ticker, report, true);
            if (result != null && !result.containsKey("null")) {
                this.data.put(report, result); // use 'report' as key
                System.out.println(ticker+" "+report+" fetched successfully");
            }
        }

        public String getTicker() {
            return this.ticker;
        }
        public HashMap<String, HashMap<String, List<String>>> getData() {
            return this.data;
        }

        @Override
        public String toString() {
            if (data == null || data.isEmpty()) {
                return "{}";
            }
            return new JSONObject(data).toString(4); // pretty print JSON
        }
    }
    public TickerData scrapScreener(String ticker) {
        TickerData tickerData = new TickerData(ticker);
        ExecutorService reportExecutor = Executors.newFixedThreadPool(reports.size());

        // Create a mapping between futures and their associated reports
        HashMap<Future<ScreenerScraper.TickerData>, String> futureToReportMap = new HashMap<>();

        // Submit each report scraping task to the executor
        for (String report : reports) {
            final String currentReport = report; // Capture for lambda
            Future<ScreenerScraper.TickerData> future = reportExecutor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    tickerData.setTickerData(currentReport);
                    return null;
                } catch (Exception e) {
                    System.err.println("Exception in task for " + ticker + " - " + currentReport + ": " + e.getMessage());
                    return null;
                }
            });
            futureToReportMap.put(future, report); // Store the mapping
        }

        // Wait for all tasks to complete
        for (Future<ScreenerScraper.TickerData> future : futureToReportMap.keySet()) {
            String reportType = futureToReportMap.get(future); // Get the report type for this future
            try {
                future.get(2, TimeUnit.SECONDS); // Can wait 2 sec at max for response
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Exception in " + Thread.currentThread().getStackTrace()[1].getMethodName()
                        + " for " + ticker + " - " + reportType + ": " + e.getMessage());
            } catch (TimeoutException e) {
                System.err.println("Timeout while processing " + ticker + " - " + reportType + ": " + e.getMessage());
            }
        }

        reportExecutor.shutdown();
        return tickerData;
    }
}
// public TickerData scrapScreener(String ticker) {
//         TickerData tickerData = new TickerData(ticker);
//         ExecutorService reportExecutor = Executors.newFixedThreadPool(reports.size());
//         List<Future<ScreenerScraper.TickerData>> reportFutures = new ArrayList<>();
//         // Submit each report scraping task to the executor
//         for (String report : reports) {
//             reportFutures.add(reportExecutor.submit(() -> {
//                 try{
//                     Thread.sleep(1000);
//                     tickerData.setTickerData(report);
//                     return null;
//                 }catch(InterruptedException e){
//                     Thread.currentThread().interrupt();
//                     System.err.println("Thread for ticker " + ticker + " report "+report+ " was interrupted: " + e.getMessage());
//                     return null;
//                 }

//             }));
//         }
//         // Wait for all tasks to complete
//         for (Future<ScreenerScraper.TickerData> future : reportFutures) {
//             try {
//                 future.get(2, TimeUnit.SECONDS); // Can wait 2 sec at max for response
//             } catch (TimeoutException |InterruptedException | ExecutionException e) {
//                 //e.printStackTrace();
//                 System.err.println("Exception in "+Thread.currentThread().getStackTrace()[1].getMethodName()+"for "+ticker);
//             }
//         }
//         reportExecutor.shutdown();

//         return tickerData;
//     }
