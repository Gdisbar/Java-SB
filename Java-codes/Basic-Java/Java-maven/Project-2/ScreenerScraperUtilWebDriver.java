// package com.javacode.webscrapper;

// import io.github.bonigarcia.wdm.WebDriverManager;
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import java.time.Duration;
// import java.util.List;
// import java.util.HashMap;

// public class ScreenerScraperUtilWebDriver {

//     public static HashMap<String, HashMap<String, String>> scrapDataUtil(String company, String report, boolean consolidated) {
//     String baseUrl = "https://www.screener.in/company";
//     String url = consolidated ? baseUrl + "/" + company + "/consolidated/#" + report
//                                : baseUrl + "/" + company + "/#" + report;

//     WebDriverManager.chromedriver().setup();
    
//     ChromeOptions options = new ChromeOptions();
//     options.addArguments("--headless"); // run in headless mode if you don't need UI
//     options.addArguments("--no-sandbox");
//     options.addArguments("--disable-dev-shm-usage");
    
//     WebDriver driver = new ChromeDriver(options);

//     HashMap<String, HashMap<String, String>> result = new HashMap<>();
    
//     try {
//         driver.get(url);

//         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//         wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".some-selector"))); // put correct selector here
        
//         // Your scraping logic here
//         // Example: scrape table or data
//         List<WebElement> rows = driver.findElements(By.cssSelector(".your-table-selector tr"));

//         for (WebElement row : rows) {
//             List<WebElement> cols = row.findElements(By.tagName("td"));
//             if (cols.size() >= 2) {
//                 String key = cols.get(0).getText();
//                 String value = cols.get(1).getText();

//                 HashMap<String, String> innerMap = new HashMap<>();
//                 innerMap.put("value", value);

//                 result.put(key, innerMap);
//             }
//         }
//     } catch (Exception e) {
//         e.printStackTrace();
//     } finally {
//         driver.quit(); // <<< VERY IMPORTANT
//     }

//     return result;
// }

// }


