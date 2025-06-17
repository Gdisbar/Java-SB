/*
## Problem 3: Immutable Value Objects with Records

**Problem Statement:**
Design an immutable object hierarchy representing a financial transaction system using Java Records. Demonstrate how records can replace traditional immutable value classes while providing better conciseness and built-in functionality.

**Requirements:**
1. Create a base `transaction.Transaction` record with common transaction attributes
2. Implement specialized transaction types (e.g., `transaction.PaymentTransaction`, `transaction.RefundTransaction`)
3. Include validation logic to ensure transaction integrity
4. Demonstrate pattern matching with records (Java 16+)
5. Implement a transaction processor that handles different transaction types differently

**Expected Solution Approach:**
Leverage Java Records for immutability, use sealed classes/interfaces for type hierarchies, and implement pattern matching for type-specific processing.

✅ Solution: Use Sealed Interface for Type Hierarchy
Use a sealed interface to allow only specific transaction subtypes:



* */
package transaction;
import java.time.LocalDateTime;
import java.math.BigDecimal;

//record Transaction(String transactionId, LocalDateTime timestamp,
//                            BigDecimal amount) {
//    public Transaction {
//        if (transactionId == null || transactionId.isBlank())
//            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
//        if (timestamp == null)
//            throw new IllegalArgumentException("Timestamp cannot be null");
//        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
//            throw new IllegalArgumentException("Amount must be positive");
//    }
//}
sealed interface Transaction permits PaymentTransaction, RefundTransaction {
    String transactionId();
    LocalDateTime timestamp();
    BigDecimal amount();
}

record PaymentTransaction(String transactionId, LocalDateTime timestamp,
                          BigDecimal amount, String payee,
                          String paymentMethod) implements Transaction {
    public PaymentTransaction {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
    }
}

record RefundTransaction(String transactionId, LocalDateTime timestamp,
                         BigDecimal amount, String originalTransactionId,
                         String reason) implements Transaction {
    public RefundTransaction {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Refund amount must be positive");
        }
        if (originalTransactionId == null || originalTransactionId.isBlank()) {
            throw new IllegalArgumentException("Original transaction ID is required");
        }
    }
}

//
//record RefundTransaction(String transactionId, LocalDateTime timestamp,
//                         BigDecimal amount, String originalTransactionId,
//                         String reason) extends Transaction {
//}

class TransactionProcessor{
    void processTransaction(Transaction transaction){
        if(transaction instanceof PaymentTransaction payment){
            System.out.println("Processing payment to: " + payment.payee() + ", amount: " + payment.amount());
        }else if(transaction instanceof RefundTransaction refund){
            System.out.println("Processing refund for original transaction: " + refund.originalTransactionId() + ", amount: " + refund.amount() + ", reason: " + refund.reason());
        }else{
            System.out.println("Unknown transaction type: " + transaction.transactionId());
        }
    }
}
public class TransactionManagement {
    public static void main(String[] args) {
        PaymentTransaction payment = new PaymentTransaction("PAY123", LocalDateTime.now(), new BigDecimal("100.50"), "Alice", "Credit Card");
        RefundTransaction refund = new RefundTransaction("REF456", LocalDateTime.now(), new BigDecimal("50.25"), "PAY123", "Damaged Goods");

        TransactionProcessor processor = new TransactionProcessor();
        processor.processTransaction(payment);
        processor.processTransaction(refund);

    }
}
