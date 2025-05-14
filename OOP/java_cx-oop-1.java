✅ Encapsulation
----------------------------------------------------------------------------
Encapsulation = Data Hiding + Controlled Access

Access to internal data is provided through getters and setters.
---------------------------------------------------------------------------
public class Employee {
    private String name;
    private int age;
    private double salary;
    
    // Getter for name
    public String getName() {
        return name;
    }
    
    // Setter for name
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }
    
    // Getter for age
    public int getAge() {
        return age;
    }
    
    // Setter for age
    public void setAge(int age) {
        if (age >= 18 && age <= 65) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age must be between 18 and 65");
        }
    }
    
    // Getter for salary
    public double getSalary() {
        return salary;
    }
    
    // Setter for salary
    public void setSalary(double salary) {
        if (salary > 0) {
            this.salary = salary;
        } else {
            throw new IllegalArgumentException("Salary must be positive");
        }
    }
}


Using Lombok framework
-------------------------------------------------------------------------------------
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

public class Customer {
    @Getter @Setter
    private Long id;
    
    @Getter @Setter @NonNull
    private String name;
    
    @Getter
    private final LocalDate registrationDate = LocalDate.now();
    
    // Custom validation in setter
    @Getter
    private String email;
    
    public void setEmail(String email) {
        if (email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}


🔒 Access Modifiers
-----------------------------------------------------------------------

public class PaymentProcessor {
    // Public API - documented and stable
    public boolean processPayment(Payment payment) {
        if (!validatePayment(payment)) {
            return false;
        }
        
        try {
            PaymentGatewayResponse response = sendToGateway(payment);
            return handleGatewayResponse(response);
        } catch (PaymentException e) {
            logError(e);
            return false;
        }
    }
    
    // Protected for subclasses to override with specific validation
    protected boolean validatePayment(Payment payment) {
        return payment != null && payment.getAmount() > 0;
    }
    
    // Package-private for related classes to access
    boolean isGatewayAvailable() {
        return checkGatewayStatus().isOperational();
    }
    
    // Private implementation details
    private PaymentGatewayResponse sendToGateway(Payment payment) {
        // Implementation details
        return PaymentGatewayConnector.send(
            formatPaymentData(payment),
            getEncryptionKey()
        );
    }
    
    private boolean handleGatewayResponse(PaymentGatewayResponse response) {
        // Implementation details
        return response.getStatus() == Status.APPROVED;
    }
    
    private void logError(Exception e) {
        Logger.getInstance().logError("Payment processing error", e);
    }
    
    private String getEncryptionKey() {
        return ConfigurationManager.getInstance().getSecureProperty("payment.key");
    }
    
    private GatewayStatus checkGatewayStatus() {
        // Implementation details
        return new GatewayMonitor().checkStatus();
    }
    
    private String formatPaymentData(Payment payment) {
        // Implementation details
        return new PaymentFormatter().format(payment);
    }
}

✅ Inheritance
-------------------------------------------------------------------------

combining hierarchical inheritance + multi-level inheritance + multiple inheritance
----------------------------------------------------------------------------------------

// Base Report
public abstract class Report {
    // Common report functionality
}

// Different report types (hierarchical inheritance)
public class FinancialReport extends Report { }
public class ComplianceReport extends Report { }

// Financial report subtypes (multi-level inheritance)
public class QuarterlyFinancialReport extends FinancialReport { }
public class AnnualFinancialReport extends FinancialReport { }

// Mix with interfaces (multiple inheritance)
public interface Exportable {
    byte[] export(ExportFormat format);
}

public interface Schedulable {
    void schedule(Frequency frequency);
}

// Combined inheritance structure
public class ScheduledQuarterlyReport extends QuarterlyFinancialReport 
    implements Exportable, Schedulable {
    // Implementations
}

🎯 super Keyword
--------------------------------------------
public abstract class HttpRequestHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    public final void handleRequest(HttpRequest request, HttpResponse response) {
        logger.debug("Processing request: {}", request.getRequestURI());
        
        try {
            // Template method pattern
            preProcess(request);
            processRequest(request, response);
            postProcess(response);
        } catch (Exception e) {
            logger.error("Error processing request", e);
            handleError(e, response);
        }
    }
    
    protected void preProcess(HttpRequest request) {
        // Default pre-processing
    }
    
    protected abstract void processRequest(HttpRequest request, HttpResponse response);
    
    protected void postProcess(HttpResponse response) {
        // Default post-processing
    }
    
    protected void handleError(Exception e, HttpResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

public class ProductApiHandler extends HttpRequestHandler {
    private final ProductService productService;
    
    public ProductApiHandler(ProductService productService) {
        this.productService = productService;
    }
    
    @Override
    protected void preProcess(HttpRequest request) {
        super.preProcess(request); // Call superclass implementation
        validateApiKey(request);
    }
    
    @Override
    protected void processRequest(HttpRequest request, HttpResponse response) {
        String productId = extractProductId(request);
        Product product = productService.getProduct(productId);
        
        if (product != null) {
            writeProductResponse(product, response);
        } else {
            response.setStatus(HttpStatus.NOT_FOUND);
        }
    }
    
    @Override
    protected void handleError(Exception e, HttpResponse response) {
        if (e instanceof InvalidApiKeyException) {
            response.setStatus(HttpStatus.UNAUTHORIZED);
        } else if (e instanceof ProductNotFoundException) {
            response.setStatus(HttpStatus.NOT_FOUND);
        } else {
            super.handleError(e, response); // Use superclass implementation for other exceptions
        }
    }
    
    // Helper methods
    private void validateApiKey(HttpRequest request) {
        // Implementation
    }
    
    private String extractProductId(HttpRequest request) {
        // Implementation
        return null;
    }
    
    private void writeProductResponse(Product product, HttpResponse response) {
        // Implementation
    }
}


🎯 Constructor Chaining in Inheritance
-------------------------------------------------------------------------------------

public class Entity {
    protected final String id;
    protected final LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    public Entity() {
        this(UUID.randomUUID().toString());
    }
    
    public Entity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
}

public class User extends Entity {
    private final String username;
    private String password;
    private UserRole role;
    private UserStatus status;
    
    public User(String username, String password) {
        // Call superclass constructor with default ID generation
        this.username = username;
        this.password = encryptPassword(password);
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }
    
    public User(String id, String username, String password, UserRole role) {
        super(id); // Use provided ID
        this.username = username;
        this.password = encryptPassword(password);
        this.role = role;
        this.status = UserStatus.PENDING_ACTIVATION;
    }
    
    private String encryptPassword(String plainText) {
        // Password encryption logic
        return BCrypt.hashpw(plainText, BCrypt.gensalt(12));
    }
}

// Even more specialized
public class AdminUser extends User {
    private List<String> managedDepartments;
    private AdminLevel adminLevel;
    
    public AdminUser(String username, String password, AdminLevel level) {
        super(username, password); // Call parent constructor
        this.role = UserRole.ADMIN; // Override the role
        this.adminLevel = level;
        this.managedDepartments = new ArrayList<>();
    }
}

✅ Polymorphism
----------------------------------------------------------------------------------
🎯 Compile-Time Polymorphism (Method Overloading)
------------------------------------------------------------------------------
public class PaymentService {
    // Process payment with minimum info
    public PaymentResult processPayment(double amount, String creditCardNumber) {
        return processPayment(amount, creditCardNumber, null, null);
    }
    
    // Process payment with currency
    public PaymentResult processPayment(double amount, String creditCardNumber, Currency currency) {
        return processPayment(amount, creditCardNumber, currency, null);
    }
    
    // Process payment with reference
    public PaymentResult processPayment(double amount, String creditCardNumber, String referenceId) {
        return processPayment(amount, creditCardNumber, null, referenceId);
    }
    
    // Full payment processing
    public PaymentResult processPayment(double amount, String creditCardNumber, 
                                       Currency currency, String referenceId) {
        // Complete implementation
        PaymentRequest request = new PaymentRequest.Builder()
            .amount(amount)
            .cardNumber(creditCardNumber)
            .currency(currency != null ? currency : Currency.getInstance("USD"))
            .reference(referenceId != null ? referenceId : UUID.randomUUID().toString())
            .build();
            
        return paymentGateway.process(request);
    }
}

🎯 Runtime Polymorphism (Method Overriding)
------------------------------------------------------------------------------------

// Strategy pattern example using polymorphism
public interface TaxCalculationStrategy {
    double calculateTax(Order order);
}

public class USATaxStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateTax(Order order) {
        double stateTax = calculateStateTax(order.getShippingAddress().getState(), order.getSubtotal());
        double federalTax = calculateFederalTax(order.getSubtotal());
        return stateTax + federalTax;
    }
    
    private double calculateStateTax(String state, double amount) {
        // Implementation
        return 0.0;
    }
    
    private double calculateFederalTax(double amount) {
        // Implementation
        return 0.0;
    }
}

public class EUTaxStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateTax(Order order) {
        return calculateVAT(order.getShippingAddress().getCountry(), order.getSubtotal());
    }
    
    private double calculateVAT(String country, double amount) {
        // Implementation
        return 0.0;
    }
}

// Usage
public class OrderService {
    private final Map<String, TaxCalculationStrategy> taxStrategies;
    
    public OrderService() {
        taxStrategies = new HashMap<>();
        taxStrategies.put("USA", new USATaxStrategy());
        taxStrategies.put("EU", new EUTaxStrategy());
        // Add more strategies
    }
    
    public Order processOrder(Order order) {
        // Determine the appropriate tax strategy
        String region = determineRegion(order.getShippingAddress());
        TaxCalculationStrategy taxStrategy = taxStrategies.getOrDefault(region, 
                                                                      new DefaultTaxStrategy());
        
        // Use polymorphism to calculate tax with the appropriate strategy
        double tax = taxStrategy.calculateTax(order);
        order.setTaxAmount(tax);
        order.setTotal(order.getSubtotal() + tax);
        
        // Continue processing order
        return order;
    }
    
    private String determineRegion(Address address) {
        // Logic to determine region
        return "USA";
    }
}

🎯 instanceof Operator
-------------------------------------------------------------------------------------
instanceof is often used for type-specific processing
---------------------------------------------------------------------------------------
public class NotificationHandler {
    public void sendNotification(Notification notification) {
        // Common processing
        validateNotification(notification);
        logNotification(notification);
        
        // Type-specific handling
        if (notification instanceof EmailNotification email) {
            sendEmail(email);
        } else if (notification instanceof SMSNotification sms) {
            sendSMS(sms);
        } else if (notification instanceof PushNotification push) {
            sendPushNotification(push);
        } else {
            throw new UnsupportedNotificationTypeException("Unsupported notification type");
        }
    }
    
    private void validateNotification(Notification notification) {
        // Validation logic
    }
    
    private void logNotification(Notification notification) {
        // Logging logic
    }
    
    private void sendEmail(EmailNotification email) {
        // Email sending logic
    }
    
    private void sendSMS(SMSNotification sms) {
        // SMS sending logic
    }
    
    private void sendPushNotification(PushNotification push) {
        // Push notification logic
    }
