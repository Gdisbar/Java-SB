package org.javacode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;

public class ScreenerScraperUtil {

    public static HashMap<String, List<String>> scrapDataUtil(String company, String report, boolean consolidated) {
        String baseUrl = "https://www.screener.in/company";
        String url = consolidated ? baseUrl + "/" + company + "/consolidated/#" + report
                : baseUrl + "/" + company + "/#" + report;

        // // HashMap<attribute,List<value>>
        HashMap<String, List<String>> result = new HashMap<>();

        try {
            // Fetch the HTML from the URL
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")  // Set a user agent to avoid 403 errors
                    .timeout(10000)            // Set timeout
                    .get();

            // Select the section where the report data is located
            Element section = doc.select("section#" + report).first();
            if (section == null) {
                System.out.println("Report section not found for: " + report);
                return result;
            }

            // Table inside the section holds the data, select all rows
            Elements rows = section.select("table tbody tr");

            for (Element row : rows) {
                Elements cells = row.select("td");

                if (cells.size() >= 2) {
                    String key = cells.get(0).text();     // First column - attribute
                    List<String> values = new ArrayList<>();
                    for (int i = 1; i < cells.size(); i++) {
                        values.add(cells.get(i).text());   // Remaining columns - values
                    }
                    result.put(key, values);
                }
            }
        } catch (IOException e) {
            System.err.println("\nError scraping : " + company + " "+report+"\n");
        }

        return result;
    }
}
