import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartGUI extends JFrame {
    private final ShoppingCart cart = SharedCart.getCart(); // shared cart
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JLabel totalLabel;

    public CartGUI() {
        setTitle("📚 Shopping Cart");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Book ID", "Title", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton clearButton = new JButton("🗑️ Clear Cart");
        totalLabel = new JLabel("Total: $0.00");

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(clearButton);
        panel.add(totalLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        clearButton.addActionListener(e -> {
            cart.clear();
            refreshTable();
        });

        refreshTable();
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            Book book = item.getBook();
            double total = item.getTotalPrice();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CartGUI::new);
    }
}
