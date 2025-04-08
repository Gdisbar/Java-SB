import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample {
    public static void main(String[] args) {
        // Database details
        String url = "jdbc:mysql://localhost:3306/ChatDB"; // Replace 'test_db' with your database
        String user = "acro0"; // Your MySQL username
        String password = ""; // Your MySQL password

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load MySQL JDBC Driver (Not required for JDBC 4.0+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");

            // Create a statement object
            stmt = conn.createStatement();

            // Execute a query
            String sql = "SELECT * FROM ChatDB.Chat_Data"; // Replace 'users' with your table
            rs = stmt.executeQuery(sql);

            // Process the result set
            System.out.println("ID   | " + "  userInput  | " + "  botResponse ");
            System.out.println("------------------------------------------------");
            while (rs.next()) {
                String sessionId = rs.getString("session_id");
                String userInput = rs.getString("user_input");
                String botResponse = rs.getString("bot_response");
                System.out.println(sessionId + " |  " + userInput + " | "+ botResponse);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}


/**

javac -cp mysql-connector-j-9.2.0.jar JDBCExample.java

java -cp ".:mysql-connector-j-9.2.0.jar" JDBCExample

**/