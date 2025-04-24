
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Customer{
    private String customerID;
    private List<Order> orderList;
    protected Customer(){}
    protected Customer(String customerID,List<Order> orderList) {
        this.customerID = customerID;
        this.orderList = orderList;
    }
//    protected Customer getCustomer(String queryID) {
//        if (queryID.equalsIgnoreCase(customerID)) {
//            return this;
//        }else{
//            return new Customer();
//        }
//    }
    protected String getCustomerID(){ return customerID; }
    protected List<Order> getorderList(){ return  orderList; }

    @Override
    public String toString() {
        return customerID;
    }
    // If two Customer objects have the same customerID, make sure
    //  Customer overrides equals() and hashCode() properly — very important
    //  for it to behave correctly as a key in the Map.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return Objects.equals(customerID, customer.customerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerID);
    }
}

class Order {
    private String orderID;
    private String trackingDetails;
    private double amount;

    protected Order(String orderID, String trackingDetails, double amount) {
        this.orderID = orderID;
        this.trackingDetails = trackingDetails;
        this.amount = amount;
    }
    protected String getOrderID(){
        return orderID;
    }

    protected String getTrackingDetails() {
        return trackingDetails;
    }

    protected double getAmount() {
        return amount;
    }

    protected boolean isCancelled() {
        return trackingDetails.equalsIgnoreCase("cancelled");
    }
    protected String extractCustomer(){
        String extractedCustomerID = orderID.split("\\$\\$")[0];
//        return new Customer().getCustomer(extractedCustomerID);
        return extractedCustomerID;
    }
}


class OrderProcessing {

    public static Map<Customer, Double> processOrders(List<Order> orders) {
        // Filter out canceled orders
        // Group by customer
        // Calculate total amount per customer
        List<Order> cancelledOrders = orders.stream()
                .filter(Order::isCancelled)
                .collect(Collectors.toList());

        Map<String, List<Order>> cancelledOrdersPerCustomer = cancelledOrders.stream()
                .collect(Collectors.groupingBy(Order::extractCustomer));

        Map<Customer, Double> totalCancelledOrders = cancelledOrdersPerCustomer
                .entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new Customer(entry.getKey(), entry.getValue()), // create Customer object from ID + orders
                        entry -> entry.getValue().stream().mapToDouble(Order::getAmount).sum() // sum of amounts
                ));
        return totalCancelledOrders;
    }

    public static void main(String[] args) {
            Order order1 = new Order("cust@1$$order1","shipped",100.0d);
            Order order2 = new Order("cust@1$$order2","cancelled",125.0d);
            Order order3 = new Order("cust@1$$order3","in-transit",133.5d);
            Order order4 = new Order("cust@1$$order4","cancelled",150.0d);
            List<Order> orderList1 = Arrays.asList(order1,order2,order3,order4);
            Customer customer1 = new Customer("cust@1",orderList1);

            Order order5 = new Order("cust@2$$order5","cancelled",125.0d);
            Order order6 = new Order("cust@2$$order6","cancelled",125.0d);
            Order order7 = new Order("cust@2$$order7","in-transit",100.0d);
            Order order8 = new Order("cust@2$$order8","cancelled",125.0d);
            List<Order> orderList2 = Arrays.asList(order5,order6,order7,order8);
            Customer customer2 = new Customer("cust@2",orderList2);

            Order order9 = new Order("cust@3$$order9", "delivered",200.0d);
            Order order10 = new Order("cust@3$$order10", "cancelled",125.0d);
            Order order11 = new Order("cust@3$$order11", "shipped",128.5d);
            Order order12 = new Order("cust@3$$order12", "in-transit",130.5d);
            List<Order> orderList3 = Arrays.asList(order9, order10, order11, order12);
            Customer customer3 = new Customer("cust@3",orderList3);

            List<Order> allOrders = new ArrayList<>(orderList1);
            allOrders.addAll(orderList2);
            allOrders.addAll(orderList3);

            Map<Customer, Double> result = processOrders(allOrders);
//            for (Map.Entry<Customer,Double> orderStatus : result.entrySet()){
//                System.out.println("Customer : "+orderStatus.getKey().getCustomerID() +" cancelled order value worth : "+orderStatus.getValue());
//            }
            result.forEach((customer, amount) ->
                System.out.println("Customer: " + customer + " | Cancelled Order Total: " + amount));
    }
}