🎯 Abstract Class
-----------------------------------------------------------------------------
Cannot be instantiated.
Can have abstract methods and concrete methods.
Abstract methods that have no implementation must be overridden by concrete subclasses.


public abstract class DataImporter {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected DataSource dataSource;
    protected ValidationService validationService;
    
    public DataImporter(DataSource dataSource, ValidationService validationService) {
        this.dataSource = dataSource;
        this.validationService = validationService;
    }
    
    // Template method pattern
    public final ImportResult importData(String source) {
        logger.info("Starting import from {}", source);
        
        try {
            // Step 1: Connect to data source
            connect(source);
            
            // Step 2: Extract data
            List<Record> records = extractData();
            logger.info("Extracted {} records", records.size());
            
            // Step 3: Transform data
            List<Entity> entities = transformData(records);
            logger.info("Transformed to {} entities", entities.size());
            
            // Step 4: Validate data
            ValidationResult validationResult = validateData(entities);
            if (!validationResult.isValid()) {
                return new ImportResult(false, 0, validationResult.getErrors());
            }
            
            // Step 5: Load data
            int savedCount = loadData(entities);
            logger.info("Successfully saved {} entities", savedCount);
            
            // Step 6: Cleanup
            cleanup();
            
            return new ImportResult(true, savedCount, Collections.emptyList());
        } catch (Exception e) {
            logger.error("Import failed", e);
            return new ImportResult(false, 0, Collections.singletonList(e.getMessage()));
        }
    }
    
    // Abstract methods that must be implemented by concrete importers
    protected abstract void connect(String source);
    protected abstract List<Record> extractData();
    protected abstract List<Entity> transformData(List<Record> records);
    protected abstract int loadData(List<Entity> entities);
    
    // Method with default implementation that can be overridden
    protected ValidationResult validateData(List<Entity> entities) {
        return validationService.validate(entities);
    }
    
    // Method with default implementation that can be overridden
    protected void cleanup() {
        // Default implementation
    }
}

// Concrete implementation
public class CSVCustomerImporter extends DataImporter {
    private final CustomerRepository customerRepository;
    private BufferedReader reader;
    
    public CSVCustomerImporter(DataSource dataSource, 
                              ValidationService validationService,
                              CustomerRepository customerRepository) {
        super(dataSource, validationService);
        this.customerRepository = customerRepository;
    }
    
    @Override
    protected void connect(String source) {
        try {
            reader = new BufferedReader(new FileReader(source));
        } catch (FileNotFoundException e) {
            throw new ImportException("Source file not found: " + source, e);
        }
    }
    
    @Override
    protected List<Record> extractData() {
        try {
            List<Record> records = new ArrayList<>();
            String line;
            // Skip header
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                records.add(new CSVRecord(line.split(",")));
            }
            
            return records;
        } catch (IOException e) {
            throw new ImportException("Error reading CSV data", e);
        }
    }
    
    @Override
    protected List<Entity> transformData(List<Record> records) {
        return records.stream()
            .map(this::convertToCustomer)
            .collect(Collectors.toList());
    }
    
    @Override
    protected int loadData(List<Entity> entities) {
        return customerRepository.saveAll((List<Customer>)(List<?>)entities).size();
    }
    
    @Override
    protected void cleanup() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            logger.warn("Error closing reader", e);
        }
    }
    
    private Customer convertToCustomer(Record record) {
        CSVRecord csvRecord = (CSVRecord) record;
        return new Customer(
            csvRecord.getValue(0), // ID
            csvRecord.getValue(1), // Name
            csvRecord.getValue(2), // Email
            csvRecord.getValue(3)  // Phone
        );
    }
}

🎯 Interface
-----------------------------------------------------------------------------


🎯 1.1 Static Variables
-------------------------------------------------------------------------------

public class ConnectionPool {
    // Static variable - shared across all instances
    private static int poolSize = 10;
    private static final List<Connection> connections = new ArrayList<>();
    
    // Instance variable
    private String poolName;
    
    static {
        // Initialize the connection pool
        try {
            for (int i = 0; i < poolSize; i++) {
                connections.add(createNewConnection());
            }
            logger.info("Connection pool initialized with {} connections", poolSize);
        } catch (SQLException e) {
            logger.error("Failed to initialize connection pool", e);
            throw new RuntimeException("Connection pool initialization failed", e);
        }
    }
    
    // Static method to get a connection from the pool
    public static synchronized Connection getConnection() {
        if (connections.isEmpty()) {
            logger.warn("Connection pool exhausted, creating new connection");
            return createNewConnection();
        }
        return connections.remove(connections.size() - 1);
    }
    
    // Static method to return a connection to the pool
    public static synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            connections.add(conn);
        }
    }
    
    private static Connection createNewConnection() {
        // Implementation for creating a new database connection
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            logger.error("Failed to create new connection", e);
            throw new RuntimeException("Failed to create new connection", e);
        }
    }
}

🎯 1.2 Static Methods
----------------------------------------------------------------

public class DateUtils {
    private static final DateTimeFormatter ISO_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter HUMAN_READABLE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a");
    
    // Private constructor to prevent instantiation
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    public static String formatAsIso(LocalDateTime dateTime) {
        return ISO_FORMATTER.format(dateTime);
    }
    
    public static String formatAsHumanReadable(LocalDateTime dateTime) {
        return HUMAN_READABLE_FORMATTER.format(dateTime);
    }
    
    public static LocalDateTime parseIsoDate(String dateString) {
        try {
            return LocalDateTime.parse(dateString, ISO_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid ISO date format: " + dateString, e);
        }
    }
    
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}

// Usage
String isoDate = DateUtils.formatAsIso(LocalDateTime.now());
LocalDateTime parsedDate = DateUtils.parseIsoDate("2023-04-15T10:30:45.123Z");


🎯 1.3 Static Block
------------------------------------------------------------------------------

public class ApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final Properties properties = new Properties();
    private static final Map<String, String> configMap = new ConcurrentHashMap<>();
    
    static {
        logger.info("Loading application configuration");
        try (InputStream input = ApplicationConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Unable to find application.properties");
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
            
            // Process properties into configuration map
            for (String key : properties.stringPropertyNames()) {
                configMap.put(key, properties.getProperty(key));
            }
            
            // Apply environment-specific overrides
            String env = System.getProperty("app.environment", "development");
            loadEnvironmentSpecificConfig(env);
            
            logger.info("Application configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load application configuration", e);
            throw new RuntimeException("Failed to load application configuration", e);
        }
    }
    
    private static void loadEnvironmentSpecificConfig(String env) {
        try (InputStream input = ApplicationConfig.class.getClassLoader()
                .getResourceAsStream("application-" + env + ".properties")) {
            if (input != null) {
                Properties envProperties = new Properties();
                envProperties.load(input);
                
                // Override with environment-specific properties
                for (String key : envProperties.stringPropertyNames()) {
                    configMap.put(key, envProperties.getProperty(key));
                }
                logger.info("Loaded environment-specific configuration for: {}", env);
            }
        } catch (IOException e) {
            logger.warn("Failed to load environment-specific configuration", e);
        }
    }
    
    public static String getConfig(String key) {
        return configMap.get(key);
    }
    
    public static String getConfig(String key, String defaultValue) {
        return configMap.getOrDefault(key, defaultValue);
    }
}

🎯 2.1 Final Variables
------------------------------------------------------------------------------

public class PaymentProcessor {
    // Final static constants
    public static final double TRANSACTION_FEE_PERCENTAGE = 0.025;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    
    // Final instance variables
    private final String merchantId;
    private final String apiKey;
    private final PaymentGateway gateway;
    
    // Final local variables
    public PaymentProcessor(String merchantId, String apiKey, PaymentGatewayType gatewayType) {
        this.merchantId = merchantId;
        this.apiKey = apiKey;
        
        // Final local variable in constructor
        final PaymentGatewayFactory factory = PaymentGatewayFactory.getInstance();
        this.gateway = factory.createGateway(gatewayType, merchantId, apiKey);
    }
    
    public TransactionResult processPayment(Payment payment) {
        // Final local variable in method
        final String transactionId = UUID.randomUUID().toString();
        
        // Using final in a loop
        for (int attempt = 1; attempt <= MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                final TransactionResult result = gateway.submitPayment(payment, transactionId);
                if (result.isSuccessful()) {
                    return result;
                }
                Thread.sleep(1000 * attempt); // Exponential backoff
            } catch (PaymentException | InterruptedException e) {
                if (attempt == MAX_RETRY_ATTEMPTS) {
                    throw new PaymentProcessingException("Payment failed after " + MAX_RETRY_ATTEMPTS + " attempts", e);
                }
            }
        }
        return new TransactionResult(false, transactionId, "Maximum retry attempts exceeded");
    }
}

🎯 2.2 Final Methods
--------------------------------------------------------------------------------

public class AuditService {
    private final AuditLogger auditLogger;
    
    public AuditService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }
    
    // Final method - critical security functionality that shouldn't be overridden
    public final void logAuditEvent(String userId, String action, String resourceId) {
        if (userId == null || action == null) {
            throw new IllegalArgumentException("User ID and action must not be null");
        }
        
        AuditEvent event = new AuditEvent.Builder()
            .withUserId(userId)
            .withAction(action)
            .withResourceId(resourceId)
            .withTimestamp(Instant.now())
            .withIpAddress(getCurrentIpAddress())
            .withSessionId(SecurityContextHolder.getContext().getSessionId())
            .build();
            
        auditLogger.log(event);
    }
    
    // Non-final method can be overridden by subclasses
    protected String getCurrentIpAddress() {
        // Implementation to get the current IP address
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .map(attrs -> ((ServletRequestAttributes) attrs).getRequest().getRemoteAddr())
            .orElse("unknown");
    }
}

// Usage in a subclass
public class EnhancedAuditService extends AuditService {
    public EnhancedAuditService(AuditLogger auditLogger) {
        super(auditLogger);
    }
    
    @Override
    protected String getCurrentIpAddress() {
        // Enhanced implementation that handles proxy servers
        String ip = super.getCurrentIpAddress();
        
        // Check for X-Forwarded-For header
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String forwardedIp = request.getHeader("X-Forwarded-For");
        
        return forwardedIp != null ? forwardedIp.split(",")[0].trim() : ip;
    }
    
    // Cannot override final method
    // @Override
    // public void logAuditEvent(String userId, String action, String resourceId) {
    //     // This would cause a compilation error
    // }
}

🎯 2.3 Final Classes
---------------------------------------------------------------------

public final class CreditCardUtils {
    private static final Logger logger = LoggerFactory.getLogger(CreditCardUtils.class);
    
    private CreditCardUtils() {
        // Private constructor to prevent instantiation
    }
    
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "Invalid card number";
        }
        
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        String maskedPart = "X".repeat(cardNumber.length() - 4);
        return maskedPart + lastFourDigits;
    }
    
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        
        // Remove any spaces or dashes
        String normalized = cardNumber.replaceAll("[\\s-]", "");
        
        // Check if all characters are digits
        if (!normalized.matches("\\d+")) {
            return false;
        }
        
        // Apply Luhn algorithm to validate
        int sum = 0;
        boolean alternate = false;
        for (int i = normalized.length() - 1; i >= 0; i--) {
            int digit = normalized.charAt(i) - '0';
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
}