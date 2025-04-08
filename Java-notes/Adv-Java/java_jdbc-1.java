### JDBC (Java Database Connectivity) - Overview  
JDBC is an API (Application Programming Interface) in Java that allows Java applications to interact with databases. It provides a set of classes and interfaces to connect, execute queries, and retrieve results from databases.

---

## **1. Steps to Connect to a Database Using JDBC**
To interact with a database using JDBC, follow these steps:

1. **Load the JDBC Driver**
2. **Establish a Connection**
3. **Create a Statement or PreparedStatement**
4. **Execute Queries**
5. **Process the Results**
6. **Close the Connection**

---

## **2. Example: Connecting to a MySQL Database and Performing CRUD Operations**
Let's assume we have a MySQL database with the following details:

- **Database Name**: `test_db`
- **Table Name**: `employees`
- **Columns**:
  - `id` (INT, Primary Key)
  - `name` (VARCHAR)
  - `salary` (DOUBLE)

### **2.1. Adding JDBC Dependency (if using Maven)**
If you're using Maven, add the MySQL JDBC Driver dependency:

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

---

### **2.2. Connecting to MySQL Database**
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "password";

        try {
            // Step 1: Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish the connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");

            // Step 6: Close the connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### **2.3. Insert Data into the Database**
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "password";

        String insertSQL = "INSERT INTO employees (id, name, salary) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Insert data
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "John Doe");
            preparedStatement.setDouble(3, 50000.0);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new employee was inserted successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### **2.4. Retrieve Data (SELECT Query)**
```java
import java.sql.*;

public class SelectData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "password";

        String selectSQL = "SELECT * FROM employees";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");

                System.out.println("ID: " + id + ", Name: " + name + ", Salary: " + salary);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### **2.5. Update Data**
```java
import java.sql.*;

public class UpdateData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "password";

        String updateSQL = "UPDATE employees SET salary = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setDouble(1, 60000.0); // New salary
            preparedStatement.setInt(2, 1); // Employee ID

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee's salary updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### **2.6. Delete Data**
```java
import java.sql.*;

public class DeleteData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "password";

        String deleteSQL = "DELETE FROM employees WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, 1); // Employee ID to delete

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

## **3. Understanding JDBC Concepts**
### **3.1. JDBC Driver Types**
There are 4 types of JDBC drivers:
1. **Type 1: JDBC-ODBC Bridge Driver** (deprecated)
2. **Type 2: Native-API Driver** (requires platform-specific libraries)
3. **Type 3: Network Protocol Driver** (translates JDBC calls into middleware)
4. **Type 4: Thin Driver** (directly communicates with the database; most commonly used)

For MySQL, we use the **Type 4 Driver**: `com.mysql.cj.jdbc.Driver`

---

### **3.2. JDBC Interfaces**
JDBC provides key interfaces such as:
- `DriverManager` → Manages database drivers and connections.
- `Connection` → Represents a connection to the database.
- `Statement` → Executes SQL queries.
- `PreparedStatement` → Precompiled SQL queries for efficiency.
- `ResultSet` → Stores retrieved data.

---

## **4. Best Practices for JDBC**
✅ Use **try-with-resources** to auto-close connections and statements.  
✅ Always use **PreparedStatement** to prevent **SQL Injection**.  
✅ Handle **SQLException** properly using **exception handling**.  
✅ Use **connection pooling** for efficient database interaction.  

---

## **5. Summary**
| Operation | JDBC Code |
|-----------|----------|
| Connect to DB | `DriverManager.getConnection()` |
| Insert Data | `executeUpdate()` with `INSERT INTO` |
| Retrieve Data | `executeQuery()` with `SELECT` |
| Update Data | `executeUpdate()` with `UPDATE` |
| Delete Data | `executeUpdate()` with `DELETE` |

---

This should give you a solid start on JDBC. Would you like to explore **connection pooling** or **transactions** next? 🚀

# **JDBC Connection Pooling - HikariCP vs Apache Commons DBCP**  

### **Why Use Connection Pooling?**
When a Java application connects to a database using JDBC, the process of establishing a connection is **expensive and time-consuming**. Creating and closing a connection for every request leads to **high latency and resource consumption**.  

**Connection Pooling** solves this problem by **reusing** database connections instead of creating a new one for every request. This reduces overhead and improves application performance.

---

## **1. What is a Connection Pool?**
A **Connection Pool** is a set of reusable database connections managed by a connection pool manager. When a request is made for a connection:
1. If an **idle connection** is available, it is returned.
2. If no idle connections exist, a new one is created (within the configured limit).
3. When a connection is closed by the application, it is **returned to the pool** instead of being physically closed.

---

## **2. Popular Connection Pooling Libraries**
There are several connection pool implementations in Java:
- **HikariCP** (High-performance, modern)
- **Apache Commons DBCP** (Older, widely used)
- **C3P0** (Less performant, not recommended for high-concurrency applications)
- **Tomcat JDBC** (Optimized for Tomcat environments)

Let's focus on **HikariCP** and **Apache Commons DBCP**, as they are the most widely used.

---

# **3. HikariCP - High-Performance Connection Pool**
HikariCP is a fast, lightweight, and robust JDBC connection pooling library. It is **the default connection pool for Spring Boot 2.x and above**.

### **✅ Advantages of HikariCP**
✔ **Fastest Connection Pool** – Performs better than Apache DBCP, C3P0  
✔ **Low Latency** – Optimized for performance (low garbage collection, fewer locks)  
✔ **Async Connection Initialization** – Avoids startup delays  
✔ **Connection Leak Detection** – Identifies and removes unused connections  

---

## **3.1. Using HikariCP in Java**
### **Step 1: Add Maven Dependency**
```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>
```

---

### **Step 2: Configure HikariCP**
```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPExample {
    public static void main(String[] args) {
        // Step 1: Configure HikariCP
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test_db");
        config.setUsername("root");
        config.setPassword("password");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(2000);
        config.setLeakDetectionThreshold(5000); // Detect connection leaks

        // Step 2: Create DataSource
        DataSource dataSource = new HikariDataSource(config);

        // Step 3: Get a Connection
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected using HikariCP!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
---

## **4. Apache Commons DBCP - Older but Reliable**
Apache Commons DBCP (Database Connection Pooling) is a widely used connection pooling implementation but is slower compared to HikariCP.

### **✅ Advantages of Apache Commons DBCP**
✔ **Mature & Stable** – Used in many enterprise applications  
✔ **Customizable** – Supports various configuration parameters  
✔ **Integration with Apache Projects** – Works well with Tomcat, Spring  

---

## **4.1. Using Apache DBCP in Java**
### **Step 1: Add Maven Dependency**
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-dbcp2</artifactId>
    <version>2.9.0</version>
</dependency>
```

---

### **Step 2: Configure Apache Commons DBCP**
```java
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ApacheDBCPExample {
    public static void main(String[] args) {
        // Step 1: Configure Apache Commons DBCP
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/test_db");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setMaxTotal(10);
        dataSource.setMinIdle(2);
        dataSource.setMaxIdle(5);
        dataSource.setMaxWaitMillis(2000); // Wait time for getting a connection

        // Step 2: Get a Connection
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected using Apache DBCP!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

# **5. HikariCP vs Apache Commons DBCP - Performance Comparison**
| Feature | **HikariCP** | **Apache Commons DBCP** |
|---------|------------|-----------------|
| **Performance** | 🚀 **Fastest** (low latency, async initialization) | 🐢 Slower than HikariCP |
| **Memory Usage** | ✅ **Low memory usage** | ❌ Higher memory consumption |
| **Connection Management** | ✅ **Advanced leak detection** | ❌ Basic leak detection |
| **Configuration Simplicity** | ✅ Simple API | ❌ More configurations required |
| **Used in Spring Boot?** | ✅ **Default pool in Spring Boot 2.x+** | ❌ Not default in Spring Boot |
| **Concurrency Handling** | ✅ Optimized for multi-threading | ❌ Higher overhead under heavy load |

---

# **6. When to Use Which?**
🔹 **Use HikariCP** if you need **high performance** and are using **Spring Boot or modern applications**.  
🔹 **Use Apache DBCP** if you're working with **legacy applications** that already rely on it.

---

## **7. Summary**
- **Connection pooling** is essential for efficient database interactions.
- **HikariCP** is the **best choice for modern Java applications** (fast, low overhead).
- **Apache DBCP** is an **older, stable** alternative but slower.
- Both can be integrated easily into **JDBC-based applications**.

---

### **Next Steps**
Would you like to integrate **HikariCP with Spring Boot** or explore **database transactions with connection pooling**? 🚀