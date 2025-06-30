import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartGUI extends JFrame {
    private final ShoppingCart cart;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JLabel totalLabel;

    public CartGUI() {
        cart = CartContext.getCart();

        setTitle("🛒 Your Cart");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Book ID", "Title", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();
        JButton clearButton = new JButton("🗑️ Clear Cart");
        JButton checkoutButton = new JButton("💳 Checkout");

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        buttonPanel.add(clearButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(totalLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        clearButton.addActionListener(e -> {
            cart.clear();
            refreshTable();
        });

        checkoutButton.addActionListener(e -> checkout());

        refreshTable();
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            Book book = item.getBook();
            double total = book.getPrice() * item.getQuantity();
            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    item.getQuantity(),
                    String.format("$%.2f", book.getPrice()),
                    String.format("$%.2f", total)
            });
        }
        totalLabel.setText(String.format("Total: $%.2f", cart.getTotalPrice()));
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        int customerId = CartContext.getCustomerId();
        if (customerId <= 0) {
            JOptionPane.showMessageDialog(this, "Invalid customer session.");
            return;
        }

        JTextField methodField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Payment Method (e.g., Card, Mpesa):"));
        panel.add(methodField);

        int confirm = JOptionPane.showConfirmDialog(this, panel, "Checkout", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.OK_OPTION) {
            String method = methodField.getText().trim();
            if (method.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a payment method.");
                return;
            }

            Order order = new Order(customerId);
            for (CartItem item : cart.getItems()) {
                order.addOrderItem(item.getBook().getBookId(), item.getQuantity(), item.getBook().getPrice());
            }

            boolean saved = order.saveOrder();
            if (saved) {
                Payment payment = new Payment(order.getOrderId(), order.getTotalPrice(), method);
                payment.processPayment();

                if (payment.isSuccessful()) {
                    DatabaseConnection.savePayment(payment);
                    JOptionPane.showMessageDialog(this, "✅ Payment Successful!\nTransaction ID: " + payment.getPaymentId());
                    CartContext.clearCart();
                    refreshTable();
                    dispose(); // ✅ Close the cart window
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Payment failed.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save order.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CartGUI::new);
    }
}
