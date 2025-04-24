
/*
* @startuml
class ConnectionPool implements AutoCloseable {
    - List<Connection> connections
    + <T> T executeQuery(String sql, Function<ResultSet, T> resultMapper)
    + <T> List<T> batchExecute(List<String> queries, Function<ResultSet, T> resultMapper)
    + close()
}

interface AutoCloseable {
    + close()
}

class Connection {
    + close()
    + prepareStatement(String sql)
    + ... other connection methods ...
}

class PreparedStatement {
    + executeQuery() : ResultSet
    + close()
    + ... other prepared statement methods ...
}

class ResultSet {
    + next() : boolean
    + getXXX(...) : YYY
    + close()
}

interface Function<T, R> {
    + apply(T) : R
}

ConnectionPool --|> AutoCloseable
ConnectionPool -- "manages" Connection
executeQuery ..> Connection : uses
executeQuery ..> PreparedStatement : uses
executeQuery ..> ResultSet : uses
batchExecute ..> Connection : uses
batchExecute ..> PreparedStatement : uses
batchExecute ..> ResultSet : uses
executeQuery ..> Function : uses
batchExecute ..> Function : uses
@enduml
*
*
* */

//interface AutoCloseable{
//    public void close();
//}
//
//
//public class AutoCloseableDBConnectionPool {
//    public static void main(String[] args) {
//
//    }
//}
