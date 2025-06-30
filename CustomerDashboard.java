import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;

public class CustomerDashboard extends JFrame {
    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private String customerName;

    public CustomerDashboard(String customerName) {
        this.customerName = customerName;
        setTitle("Welcome, " + customerName);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        Font font = new Font("Segoe UI", Font.PLAIN, 15);
        Color bg = new Color(250, 250, 255);
        getContentPane().setBackground(bg);

        String[] columns = {"Book ID", "Title", "Author", "Price", "Stock"};
        booksTableModel = new DefaultTableModel(columns, 0);
        booksTable = new JTable(booksTableModel);
        booksTable.setFont(font);
        booksTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Books"));
        add(scrollPane, BorderLayout.CENTER);

        JButton buyBtn = new JButton("🛒 Add to Cart");
        JButton viewCartBtn = new JButton("🛍️ View Cart");
        JButton logoutBtn = new JButton("🚪 Logout"); // ✅ Added Logout Button

        buyBtn.setFont(font);
        viewCartBtn.setFont(font);
        logoutBtn.setFont(font);

        buyBtn.addActionListener(e -> addToCart());
        viewCartBtn.addActionListener(e -> new CartGUI());
        logoutBtn.addActionListener(e -> {
            dispose(); // Close dashboard
            new LoginScreen(); // ✅ Go back to login screen
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(bg);
        bottomPanel.add(buyBtn);
        bottomPanel.add(viewCartBtn);
        bottomPanel.add(logoutBtn); // ✅ Add Logout to Panel

        add(bottomPanel, BorderLayout.SOUTH);
        loadBooks();
        setVisible(true);
    }

    private void loadBooks() {
        booksTableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT book_id, title, author, price, stock FROM books")) {

            while (rs.next()) {
                booksTableModel.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load books.");
        }
    }

    private void addToCart() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book.");
            return;
        }

        int bookId = (int) booksTableModel.getValueAt(selectedRow, 0);
        String title = (String) booksTableModel.getValueAt(selectedRow, 1);
        String author = (String) booksTableModel.getValueAt(selectedRow, 2);
        BigDecimal price = (BigDecimal) booksTableModel.getValueAt(selectedRow, 3);
        int stock = (int) booksTableModel.getValueAt(selectedRow, 4);

        JTextField quantityField = new JTextField("1");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Book: " + title));
        panel.add(new JLabel("Stock: " + stock));
        panel.add(new JLabel("Quantity to Add:"));
        panel.add(quantityField);

        int confirm = JOptionPane.showConfirmDialog(this, panel, "Add to Cart", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0 || quantity > stock) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }

            Book book = new Book(bookId, title, author, "Genre", "Category", price.doubleValue(), stock);
            ShoppingCart cart = CartContext.getCart();
            cart.addItem(book, quantity);

            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement updateStock = conn.prepareStatement(
                        "UPDATE books SET stock = stock - ? WHERE book_id = ? AND stock >= ?"
                );
                updateStock.setInt(1, quantity);
                updateStock.setInt(2, bookId);
                updateStock.setInt(3, quantity);

                int updated = updateStock.executeUpdate();
                if (updated == 0) {
                    JOptionPane.showMessageDialog(this, "Failed to update stock.");
                    return;
                }
            }

            loadBooks();
            JOptionPane.showMessageDialog(this, "Book added to cart and stock updated!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
}
