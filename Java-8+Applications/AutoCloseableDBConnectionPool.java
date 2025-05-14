import java.sql.*;
import java.util.*;
import java.util.function.Function;

class ConnectionPool implements AutoCloseable {
    private final List<Connection> connections = new ArrayList<>();
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/APITest";
    private final String user = "acro0"; // Change if different
    private final String password = ""; // Change accordingly

    public ConnectionPool(int poolSize) throws SQLException {
        for (int i = 0; i < poolSize; i++) {
            connections.add(DriverManager.getConnection(jdbcUrl, user, password));
        }
    }

    private Connection getConnection() throws SQLException {
        if (!connections.isEmpty()) {
            return connections.remove(0);
        } else {
            throw new SQLException("No available connections in pool");
        }
    }

    private void returnConnection(Connection conn) {
        connections.add(conn);
    }

    public <T> T executeQuery(String sql, Function<ResultSet, T> resultMapper) {
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            T result = resultMapper.apply(rs);
            rs.close();
            stmt.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error executing query", e);
        } finally {
            if (conn != null) returnConnection(conn);
        }
    }

    public <T> List<T> batchExecute(List<String> queries, Function<ResultSet, T> resultMapper) {
        List<T> results = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            for (String sql : queries) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    results.add(resultMapper.apply(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in batch execution", e);
        } finally {
            if (conn != null) returnConnection(conn);
        }
        return results;
    }

    @Override
    public void close() {
        for (Connection conn : connections) {
            try {
                conn.close();
            } catch (SQLException ignored) {}
        }
        connections.clear();
    }
}


class AutoCloseableDBConnectionPool{
    public static void main(String[] args) {
        try (ConnectionPool pool = new ConnectionPool(3)) {
            String sql = "SELECT * FROM users";

            List<String> queries = Arrays.asList(
                    "SELECT * FROM users WHERE id = 2",
                    "SELECT * FROM users WHERE id = 3"
            );

            List<String> names = pool.executeQuery(sql, rs -> {
                List<String> list = new ArrayList<>();
                try {
                    while (rs.next()) {
                        list.add(rs.getString("name"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return list;
            });

            System.out.println("Names: " + names);

            List<String> emails = pool.batchExecute(queries, rs -> {
                try {
                    if (rs.next()) {
                        return rs.getString("email");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });

            System.out.println("Emails: " + emails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}