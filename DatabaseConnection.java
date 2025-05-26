package BookNestApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/booknestapp_db"; // Make sure this matches your DB name and port
        String user = "root";
        String password = "root"; // Default password for XAMPP is usually empty

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensures JDBC driver is loaded
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}
