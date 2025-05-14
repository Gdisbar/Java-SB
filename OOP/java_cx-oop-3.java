🎯 Interface
-----------------------------------------------------------------------------

// Service interfaces
public interface UserService {
    User findById(String id);
    List<User> findByRole(UserRole role);
    User create(UserRegistrationRequest request);
    User update(UserUpdateRequest request);
    void delete(String id);
}

public interface AuthenticationService {
    AuthenticationResult authenticate(String username, String password);
    void logout(String sessionId);
    boolean verifyToken(String token);
}

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendTemplatedEmail(String to, String template, Map<String, Object> variables);
}

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder,
                         EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
    
    @Override
    public User findById(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    @Override
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }
    
    @Override
    public User create(UserRegistrationRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        // Send welcome email
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", user.getUsername());
        emailService.sendTemplatedEmail(user.getEmail(), "welcome-template", variables);
        
        return savedUser;
    }
    
    @Override
    public User update(UserUpdateRequest request) {
        User user = findById(request.getId());
        
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Override
    public void delete(String id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final SessionRepository sessionRepository;
    
    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   JwtTokenProvider tokenProvider,
                                   SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.sessionRepository = sessionRepository;
    }
    
    @Override
    public AuthenticationResult authenticate(String username, String password) {
        // Find user by username
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        
        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        // Generate JWT token
        String token = tokenProvider.generateToken(user);
        
        // Create session
        Session session = new Session();
        session.setUserId(user.getId());
        session.setToken(token);
        session.setCreatedAt(LocalDateTime.now());
        sessionRepository.save(session);
        
        return new AuthenticationResult(token, user);
    }
    
    @Override
    public void logout(String sessionId) {
        sessionRepository.findById(sessionId)
            .ifPresent(session -> {
                sessionRepository.delete(session);
            });
    }
    
    @Override
    public boolean verifyToken(String token) {
        return tokenProvider.validateToken(token) && 
               sessionRepository.existsByToken(token);
    }
}

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send email", e);
        }
    }
    
    @Override
    public void sendTemplatedEmail(String to, String template, Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        
        String processedTemplate = templateEngine.process(template, context);
        sendEmail(to, "Welcome to Our Service", processedTemplate);
    }
}

🎯 1.1 Static Nested Class
----------------------------------------------------------------------------------
public class OrderProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);
    
    // Static nested class
    public static class OrderBuilder {
        private String orderId;
        private String customerId;
        private List<OrderItem> items = new ArrayList<>();
        private ShippingAddress shippingAddress;
        private PaymentMethod paymentMethod;
        
        public OrderBuilder withOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }
        
        public OrderBuilder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }
        
        public OrderBuilder addItem(OrderItem item) {
            this.items.add(item);
            return this;
        }
        
        public OrderBuilder withShippingAddress(ShippingAddress address) {
            this.shippingAddress = address;
            return this;
        }
        
        public OrderBuilder withPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }
        
        public Order build() {
            if (orderId == null || customerId == null || items.isEmpty()) {
                throw new IllegalStateException("Order must have an ID, customer ID, and at least one item");
            }
            
            Order order = new Order(orderId, customerId);
            order.setItems(items);
            order.setShippingAddress(shippingAddress);
            order.setPaymentMethod(paymentMethod);
            
            return order;
        }
    }
    
    // Methods of the outer class
    public void processOrder(Order order) {
        logger.info("Processing order: {}", order.getOrderId());
        // Order processing logic
    }
}

// Usage
Order order = new OrderProcessor.OrderBuilder()
    .withOrderId("ORD-12345")
    .withCustomerId("CUST-789")
    .addItem(new OrderItem("PROD-001", 2, 19.99))
    .addItem(new OrderItem("PROD-042", 1, 34.99))
    .withShippingAddress(new ShippingAddress("123 Main St", "Anytown", "CA", "12345"))
    .withPaymentMethod(PaymentMethod.CREDIT_CARD)
    .build();

OrderProcessor processor = new OrderProcessor();
processor.processOrder(order);


🎯 1.2 Non-Static Inner Class
-----------------------------------------------------------------------------------

public class CacheManager {
    private final Map<String, CacheEntry<?>> cache = new ConcurrentHashMap<>();
    private final long defaultTtlMillis;
    private final ScheduledExecutorService cleanupExecutor;
    
    public CacheManager(long defaultTtlMillis) {
        this.defaultTtlMillis = defaultTtlMillis;
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
        
        // Schedule periodic cleanup
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredEntries, 1, 1, TimeUnit.MINUTES);
    }
    
    public <T> void put(String key, T value) {
        put(key, value, defaultTtlMillis);
    }
    
    public <T> void put(String key, T value, long ttlMillis) {
        cache.put(key, new CacheEntry<>(value, ttlMillis));
    }
    
    public <T> Optional<T> get(String key, Class<T> type) {
        CacheEntry<?> entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            if (entry != null && entry.isExpired()) {
                cache.remove(key);
            }
            return Optional.empty();
        }
        
        // Type safety check
        if (!type.isInstance(entry.getValue())) {
            throw new ClassCastException("Cached value is not of type " + type.getName());
        }
        
        return Optional.of(type.cast(entry.getValue()));
    }
    
    private void cleanupExpiredEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    // Non-static inner class
    private class CacheEntry<T> {
        private final T value;
        private final long expirationTime;
        
        public CacheEntry(T value, long ttlMillis) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + ttlMillis;
        }
        
        public T getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}

// Usage
CacheManager cacheManager = new CacheManager(60000); // 1 minute default TTL
cacheManager.put("user_profile_123", userProfile);
Optional<UserProfile> cachedProfile = cacheManager.get("user_profile_123", UserProfile.class);


🎯 1.3 Local Inner Class
----------------------------------------------------------------------------------


public class DataProcessor {
    private final Logger logger = LoggerFactory.getLogger(DataProcessor.class);
    
    public List<String> processRecords(List<Record> records, String filterField, String filterValue) {
        // Local inner class defined inside a method
        class RecordFilter {
            private final String field;
            private final String value;
            
            RecordFilter(String field, String value) {
                this.field = field;
                this.value = value;
            }
            
            boolean matches(Record record) {
                Object fieldValue = record.getField(field);
                if (fieldValue == null) {
                    return false;
                }
                return fieldValue.toString().equals(value);
            }
        }
        
        RecordFilter filter = new RecordFilter(filterField, filterValue);
        
        List<String> result = new ArrayList<>();
        for (Record record : records) {
            if (filter.matches(record)) {
                try {
                    String processedData = processRecord(record);
                    result.add(processedData);
                } catch (Exception e) {
                    logger.error("Failed to process record {}: {}", record.getId(), e.getMessage());
                }
            }
        }
        
        return result;
    }
    
    private String processRecord(Record record) {
        // Record processing logic
        return "Processed: " + record.toString();
    }
}


🎯 1.4 Anonymous Inner Class
----------------------------------------------------------------------------------

public class UIController {
    private final TaskExecutor taskExecutor;
    
    public UIController(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    public void downloadFile(String fileUrl, String destinationPath) {
        Button downloadButton = new Button("Download");
        
        // Anonymous inner class to handle button click
        downloadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable button during download
                downloadButton.setEnabled(false);
                downloadButton.setText("Downloading...");
                
                // Execute download task in background
                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileDownloader downloader = new FileDownloader();
                            boolean success = downloader.download(fileUrl, destinationPath);
                            
                            // Update UI on main thread
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        downloadButton.setText("Download Complete");
                                    } else {
                                        downloadButton.setText("Download Failed");
                                        downloadButton.setEnabled(true);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            logger.error("Download failed", e);
                            
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    downloadButton.setText("Download Failed");
                                    downloadButton.setEnabled(true);
                                    showErrorDialog("Download failed: " + e.getMessage());
                                }
                            });
                        }
                    }
                });
            }
        });
        
        // With Java 8+ lambda syntax (preferred over anonymous inner classes)
        Button modernButton = new Button("Download");
        modernButton.setOnClickListener(view -> {
            modernButton.setEnabled(false);
            modernButton.setText("Downloading...");
            
            taskExecutor.execute(() -> {
                try {
                    FileDownloader downloader = new FileDownloader();
                    boolean success = downloader.download(fileUrl, destinationPath);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            modernButton.setText("Download Complete");
                        } else {
                            modernButton.setText("Download Failed");
                            modernButton.setEnabled(true);
                        }
                    });
                } catch (Exception e) {
                    logger.error("Download failed", e);
                    
                    SwingUtilities.invokeLater(() -> {
                        modernButton.setText("Download Failed");
                        modernButton.setEnabled(true);
                        showErrorDialog("Download failed: " + e.getMessage());
                    });
                }
            });
        });
    }
    
    private void showErrorDialog(String message) {
        // Implementation of error dialog
    }
}

