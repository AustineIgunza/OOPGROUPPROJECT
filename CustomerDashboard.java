import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

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

        JButton buyBtn = new JButton("🛒 Buy Selected Book");
        buyBtn.setFont(font);
        buyBtn.addActionListener(e -> buySelectedBook());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(bg);
        bottomPanel.add(buyBtn);

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

    private void buySelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to buy.");
            return;
        }

        int modelRow = booksTable.convertRowIndexToModel(selectedRow);
        int bookId = (int) booksTableModel.getValueAt(modelRow, 0);
        String title = (String) booksTableModel.getValueAt(modelRow, 1);
        BigDecimal price = (BigDecimal) booksTableModel.getValueAt(modelRow, 3);
        int stock = (int) booksTableModel.getValueAt(modelRow, 4);

        JTextField quantityField = new JTextField("1");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Book: " + title));
        panel.add(new JLabel("Price: $" + price));
        panel.add(new JLabel("Stock: " + stock));
        panel.add(new JLabel("Enter quantity to buy:"));
        panel.add(quantityField);

        int confirm = JOptionPane.showConfirmDialog(this, panel, "Buy Book", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0 || quantity > stock) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement checkStock = conn.prepareStatement("SELECT stock FROM books WHERE book_id = ?");
            checkStock.setInt(1, bookId);
            ResultSet rs = checkStock.executeQuery();
            if (!rs.next() || rs.getInt("stock") < quantity) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Insufficient stock. Try again.");
                return;
            }

            int customerId = -1;
            PreparedStatement custStmt = conn.prepareStatement("SELECT customer_id FROM customers WHERE name = ?");
            custStmt.setString(1, customerName);
            ResultSet custRs = custStmt.executeQuery();
            if (custRs.next()) {
                customerId = custRs.getInt("customer_id");
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Customer not found.");
                return;
            }

            PreparedStatement orderStmt = conn.prepareStatement(
                    "INSERT INTO orders (customer_id, order_date, status) VALUES (?, NOW(), 'Pending')",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            orderStmt.setInt(1, customerId);
            orderStmt.executeUpdate();

            ResultSet keys = orderStmt.getGeneratedKeys();
            int orderId = -1;
            if (keys.next()) {
                orderId = keys.getInt(1);
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Order creation failed.");
                return;
            }

            PreparedStatement itemStmt = conn.prepareStatement(
                    "INSERT INTO order_items (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)"
            );
            itemStmt.setInt(1, orderId);
            itemStmt.setInt(2, bookId);
            itemStmt.setInt(3, quantity);
            itemStmt.setBigDecimal(4, price);
            itemStmt.executeUpdate();

            PreparedStatement updateStock = conn.prepareStatement(
                    "UPDATE books SET stock = stock - ? WHERE book_id = ?"
            );
            updateStock.setInt(1, quantity);
            updateStock.setInt(2, bookId);
            updateStock.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Purchase successful! Order ID: " + orderId);
            loadBooks();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Transaction failed: " + e.getMessage());
        }
    }
}
