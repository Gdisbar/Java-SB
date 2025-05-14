import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Transaction{
    private String category;
    private double amount;
    protected Transaction(String category,double amount){
        this.category=category;
        this.amount=amount;
    }
    protected String getCategory() {
        return category;
    }

    protected double getAmount() {
        return amount;
    }
}

class TransactionAnalyzerMethods {
    protected Map<String, Double> getCategoryTotals(List<Transaction> transactions) {
        // Group transactions by category and sum the amounts
        Map<String, Double> groupedTransactionAmount = transactions.stream()
                .collect(Collectors
                        .groupingBy(Transaction::getCategory,
                                Collectors
                                        .summingDouble(Transaction::getAmount)));
        return groupedTransactionAmount;
    }

    protected Map<String, CustomStatistics> getCategoryStatistics(List<Transaction> transactions) {
        // Group by category and collect statistics (count, sum, min, max, average)
        Map<String, DoubleSummaryStatistics> groupedStat = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summarizingDouble(Transaction::getAmount)));

        Map<String, CustomStatistics> groupedStatCustom = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.collectingAndThen(Collectors.summarizingDouble(Transaction::getAmount),
                                CustomStatistics::fromSummaryStatistics)));
        return groupedStatCustom;
    }

}

class CustomStatistics{
    private long count;
    private double sum;
    private double min;
    private double average;
    private double max;

    public CustomStatistics(long count, double sum, double min, double average, double max) {
        this.count = count;
        this.sum = sum;
        this.min = min;
        this.average = average;
        this.max = max;
    }

    protected static CustomStatistics fromSummaryStatistics(DoubleSummaryStatistics stats) {
        return new CustomStatistics(stats.getCount(), stats.getSum(), stats.getMin(), stats.getAverage(), stats.getMax());
    }

    @Override
    public String toString() {
        return String.format("[count=%d, sum=%.6f, min=%.6f, average=%.6f, max=%.6f]",
                count, sum, min, average, max);
    }
}

class TransactionAnalyzer {
    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("Groceries", 55.75));
        transactions.add(new Transaction("Electronics", 120.50));
        transactions.add(new Transaction("Groceries", 23.20));
        transactions.add(new Transaction("Dining", 38.90));
        transactions.add(new Transaction("Books", 15.99));
        transactions.add(new Transaction("Electronics", 350.00));
        transactions.add(new Transaction("Utilities", 78.45));
        transactions.add(new Transaction("Dining", 62.15));
        transactions.add(new Transaction("Groceries", 12.80));
        transactions.add(new Transaction("Entertainment", 29.99));

        TransactionAnalyzerMethods transactionAnalyzerMethods = new TransactionAnalyzerMethods();
        Map<String, Double> result = transactionAnalyzerMethods.getCategoryTotals(transactions);
        result.forEach((category,totalAmount)->
                System.out.println("Category : "+category+" || Total Amount : "+totalAmount));
        System.out.println("=======================");
        Map<String, CustomStatistics> resultStat = transactionAnalyzerMethods.getCategoryStatistics(transactions);
        resultStat.forEach((category,statistics)->
                System.out.println("Category : "+category+" || Stats : "+statistics.toString()));
    }
}
