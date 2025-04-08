# **Transaction Management in JDBC**  
Transaction Management ensures that a series of database operations **either complete successfully together (commit) or fail together (rollback)**, maintaining **data consistency and integrity**.

---

## **1. What is a Transaction?**
A **transaction** is a sequence of SQL operations executed as a **single unit of work**. It follows **ACID properties**:  

| **Property** | **Description** |
|-------------|----------------|
| **Atomicity** | All operations complete successfully, or none at all (rollback on failure). |
| **Consistency** | The database remains in a valid state before and after the transaction. |
| **Isolation** | Transactions execute independently, avoiding conflicts. |
| **Durability** | Changes persist permanently after commit, even in case of system failure. |

---

## **2. Default Behavior in JDBC**
- By default, **JDBC operates in auto-commit mode**, meaning each SQL statement is executed as an individual transaction.
- This is inefficient for **batch updates or multiple dependent operations**.

✅ **Solution:** Use **manual transaction management** by disabling auto-commit.

---

## **3. Transaction Management in JDBC**
### **Example 1: Manual Transaction Management**
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "password";

        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
            // Step 1: Establish a Connection
            conn = DriverManager.getConnection(url, user, password);

            // Step 2: Disable Auto-Commit
            conn.setAutoCommit(false);

            // Step 3: Execute SQL Statements
            String sql1 = "INSERT INTO accounts (id, name, balance) VALUES (1, 'Alice', 1000)";
            stmt1 = conn.prepareStatement(sql1);
            stmt1.executeUpdate();

            String sql2 = "INSERT INTO accounts (id, name, balance) VALUES (2, 'Bob', 1500)";
            stmt2 = conn.prepareStatement(sql2);
            stmt2.executeUpdate();

            // Step 4: Commit the Transaction
            conn.commit();
            System.out.println("Transaction committed successfully.");

        } catch (SQLException e) {
            try {
                // Step 5: Rollback in case of error
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to error.");
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Step 6: Close resources
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
```
---

## **4. Key Steps in JDBC Transaction Management**
1. **Disable Auto-Commit:** `conn.setAutoCommit(false);`
2. **Execute SQL Queries:** Use `PreparedStatement.executeUpdate()`
3. **Commit Changes:** `conn.commit();`
4. **Handle Errors and Rollback:** `conn.rollback();` (if an exception occurs)
5. **Close Resources:** `stmt.close(); conn.close();`

---

## **5. Example 2: Handling a Money Transfer Scenario**
Imagine **Alice sends ₹500 to Bob**. The transaction should:
- Deduct ₹500 from Alice’s balance.
- Add ₹500 to Bob’s balance.
- Ensure **both operations succeed together** or **get rolled back on failure**.

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankTransaction {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bank_db";
        String user = "root";
        String password = "password";

        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);  // Disable auto-commit

            // Deduct ₹500 from Alice's account
            String withdrawSQL = "UPDATE accounts SET balance = balance - 500 WHERE name = 'Alice'";
            stmt1 = conn.prepareStatement(withdrawSQL);
            stmt1.executeUpdate();

            // Add ₹500 to Bob's account
            String depositSQL = "UPDATE accounts SET balance = balance + 500 WHERE name = 'Bob'";
            stmt2 = conn.prepareStatement(depositSQL);
            stmt2.executeUpdate();

            // Commit the transaction
            conn.commit();
            System.out.println("Transaction Successful: ₹500 transferred from Alice to Bob.");

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();  // Rollback transaction on failure
                    System.out.println("Transaction Failed: Rolled back.");
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
```

---

## **6. Isolation Levels in JDBC Transactions**
Isolation levels **define how transactions interact with each other**. JDBC supports the following levels:

| **Isolation Level** | **Description** | **Issues Prevented** |
|--------------------|---------------|----------------------|
| `READ_UNCOMMITTED` | Allows reading uncommitted data (dirty reads possible). | ❌ No prevention |
| `READ_COMMITTED` | Prevents dirty reads but allows non-repeatable reads. | ✅ Prevents Dirty Reads |
| `REPEATABLE_READ` | Prevents dirty & non-repeatable reads, but allows phantom reads. | ✅ Prevents Dirty & Non-repeatable Reads |
| `SERIALIZABLE` | Fully isolates transactions (slowest but safest). | ✅ Prevents Dirty, Non-repeatable & Phantom Reads |

### **Setting Isolation Level in JDBC**
```java
conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
```

---

## **7. Using Transaction Management with HikariCP**
HikariCP automatically manages transactions efficiently, but you can still control them manually.

```java
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/bank_db");
config.setUsername("root");
config.setPassword("password");
config.setMaximumPoolSize(10);

HikariDataSource dataSource = new HikariDataSource(config);

try (Connection conn = dataSource.getConnection()) {
    conn.setAutoCommit(false);

    try (PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET balance = balance - 500 WHERE name = 'Alice'")) {
        stmt.executeUpdate();
    }

    try (PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET balance = balance + 500 WHERE name = 'Bob'")) {
        stmt.executeUpdate();
    }

    conn.commit();
    System.out.println("Transaction Successful!");

} catch (SQLException e) {
    e.printStackTrace();
}
```

---

## **8. Summary**
🔹 **JDBC Transactions** ensure that **multiple database operations** execute as a single unit.  
🔹 **Disable Auto-Commit** to handle transactions manually (`conn.setAutoCommit(false)`).  
🔹 **Use `commit()` and `rollback()`** to control transaction success/failure.  
🔹 **Choose Isolation Levels** to control concurrency issues.  
🔹 **HikariCP** and other connection pools **improve performance** while handling transactions efficiently.

---

## **Next Steps**
Would you like to integrate **JDBC transaction management in a Spring Boot application** or explore **optimizing transactions for high-concurrency systems**? 🚀