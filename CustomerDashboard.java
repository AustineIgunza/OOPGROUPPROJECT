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
        JButton viewCartBtn = new JButton("🧺 View Cart");

        buyBtn.setFont(font);
        viewCartBtn.setFont(font);

        buyBtn.addActionListener(e -> buySelectedBook());
        viewCartBtn.addActionListener(e -> new CartGUI());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(bg);
        bottomPanel.add(buyBtn);
        bottomPanel.add(viewCartBtn);

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
            JOptionPane.showMessageDialog(this, "Please select a book.");
            return;
        }

        int modelRow = booksTable.convertRowIndexToModel(selectedRow);
        int bookId = (int) booksTableModel.getValueAt(modelRow, 0);
        String title = (String) booksTableModel.getValueAt(modelRow, 1);
        String author = (String) booksTableModel.getValueAt(modelRow, 2);
        BigDecimal price = (BigDecimal) booksTableModel.getValueAt(modelRow, 3);
        int stock = (int) booksTableModel.getValueAt(modelRow, 4);

        JTextField quantityField = new JTextField("1");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Book: " + title));
        panel.add(new JLabel("Price: $" + price));
        panel.add(new JLabel("Stock: " + stock));
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int confirm = JOptionPane.showConfirmDialog(this, panel, "Add to Cart", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0 || quantity > stock) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }

            Book book = new Book(bookId, title, author, "Unknown", "Unknown", price.doubleValue(), stock);
            SharedCart.getCart().addItem(book, quantity);
            JOptionPane.showMessageDialog(this, "Book added to cart.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }
}
