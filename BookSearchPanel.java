import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BookSearchPanel extends JPanel {

    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable resultTable;

    public BookSearchPanel() {
        setLayout(new BorderLayout(10, 10));

        // Top: Search Input
        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");
        topPanel.add(new JLabel("Search by Title, Author, or Genre:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Center: Table for results
        String[] cols = {"Book ID", "Title", "Author", "Genre", "Price", "Stock"};
        tableModel = new DefaultTableModel(cols, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        // Bottom: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addToCartBtn = new JButton("🛒 Add to Cart");
        JButton viewCartBtn = new JButton("💾 View Cart");
        JButton logoutBtn = new JButton("🚪 Logout");
        buttonPanel.add(addToCartBtn);
        buttonPanel.add(viewCartBtn);
        buttonPanel.add(logoutBtn);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        searchButton.addActionListener(e -> performSearch());
        addToCartBtn.addActionListener(e -> addSelectedToCart());
        viewCartBtn.addActionListener(e -> new CartGUI());
        logoutBtn.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new LoginScreen();
        });
    }

    private void performSearch() {
        tableModel.setRowCount(0);
        String term = searchField.getText().trim();
        if (term.isEmpty()) return;

        String sql = "SELECT book_id, title, author, genre, price, stock FROM books" +
                " WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            String like = "%" + term + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
        }
    }

    private void addSelectedToCart() {
        int r = resultTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a book first!");
            return;
        }

        int bookId = (int) tableModel.getValueAt(r, 0);
        String title = (String) tableModel.getValueAt(r, 1);
        String author = (String) tableModel.getValueAt(r, 2);
        String genre = (String) tableModel.getValueAt(r, 3);
        double price = (double) tableModel.getValueAt(r, 4);
        int stock = (int) tableModel.getValueAt(r, 5);

        String qtyStr = JOptionPane.showInputDialog(this, "Quantity:", "1");
        if (qtyStr == null) return;

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty<1 || qty>stock) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }

            Book book = new Book(bookId, title, author, genre, "Category", price, stock);
            ShoppingCart cart = CartContext.getCart();
            cart.addItem(book, qty);

            // update stock in DB
            try (PreparedStatement ps = DatabaseConnection.getConnection()
                    .prepareStatement("UPDATE books SET stock = stock - ? WHERE book_id = ? AND stock >= ?")) {
                ps.setInt(1, qty);
                ps.setInt(2, bookId);
                ps.setInt(3, qty);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Added to cart!");
            performSearch(); // refresh stock

        } catch (NumberFormatException|SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
