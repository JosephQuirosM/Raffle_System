package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
     private static final String url = "jdbc:oracle:thin:@localhost:1521:XE"; //jdbc:oracle:thin:@//localhost:1521/XE
    private static final String user = "RIFA";
    private static final String password = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password); 
    }
}
