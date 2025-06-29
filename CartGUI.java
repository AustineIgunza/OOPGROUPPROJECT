import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class CartGUI extends JFrame {
    private final ShoppingCart cart;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JLabel totalLabel;

    public CartGUI() {
        cart = new ShoppingCart();

        setTitle("Bookstore Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table for cart
        String[] columns = {"Book ID", "Title", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Book");
        JButton clearButton = new JButton("Clear Cart");

        totalLabel = new JLabel("Total: $0.00");

        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(totalLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addBookDialog());
        clearButton.addActionListener(e -> {
            cart.clear();
            refreshTable();
        });

        refreshTable();
        setVisible(true);
    }

    private void addBookDialog() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Book ID:"));
        panel.add(idField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Book to Cart",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String title = titleField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                Book book = new Book(id, title, "Unknown", "Genre", "Category", price, 70);
                cart.addItem(book, quantity);
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            Book book = item.getBook();
            double total = book.getPrice() * item.getQuantity();
            tableModel.addRow(new Object[] {
                    book.getBookId(),
                    book.getTitle(),
                    item.getQuantity(),
                    String.format("$%.2f", book.getPrice()),
                    String.format("$%.2f", total)
            });
        }
        totalLabel.setText(String.format("Total: $%.2f", cart.getTotalPrice()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CartGUI::new);
    }
}
