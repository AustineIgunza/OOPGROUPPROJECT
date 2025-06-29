import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/MyBookstoreDB";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Fetch all books from DB
    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Test database connection method
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed! See error below:");
            e.printStackTrace();
        }
    }

    // You can add a main method to run the test standalone
    public static void main(String[] args) {
        testConnection();
    }
}