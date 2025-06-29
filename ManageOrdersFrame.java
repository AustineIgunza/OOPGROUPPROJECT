import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageOrdersFrame extends JFrame {
    private DefaultTableModel orderTableModel;
    private JTable orderTable;

    public ManageOrdersFrame() {
        setTitle("📦 Manage Orders");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        Color backgroundColor = new Color(245, 245, 255);
        Color primaryColor = new Color(63, 81, 181);
        Font font = new Font("Segoe UI", Font.PLAIN, 15);

        getContentPane().setBackground(backgroundColor);

        // Table Setup
        String[] columns = {"Order ID", "Customer", "Book Title", "Quantity", "Status"};
        orderTableModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(orderTableModel);
        orderTable.setFont(font);
        orderTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Order List"));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton updateBtn = createStyledButton("✅ Mark as Shipped", primaryColor, font);
        JButton deleteBtn = createStyledButton("🗑️ Delete Order", primaryColor, font);
        JButton refreshBtn = createStyledButton("🔄 Refresh", primaryColor, font);

        updateBtn.addActionListener(e -> updateOrderStatus());
        deleteBtn.addActionListener(e -> deleteSelectedOrder());
        refreshBtn.addActionListener(e -> loadOrders());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        loadOrders();
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color, Font font) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(Color.WHITE);
        button.setForeground(color);
        button.setBorder(BorderFactory.createLineBorder(color, 2, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadOrders() {
        orderTableModel.setRowCount(0);
        List<OrderSummary> orders = DatabaseConnection.getAllOrderSummaries();
        for (OrderSummary order : orders) {
            orderTableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getBookTitle(),
                    order.getQuantity(),
                    order.getStatus()
            });
        }
    }

    private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = orderTableModel.getValueAt(selectedRow, 0).toString();
            if (DatabaseConnection.markOrderAsShipped(orderId)) {
                orderTableModel.setValueAt("Shipped", selectedRow, 4);
                JOptionPane.showMessageDialog(this, "Order marked as Shipped.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update order status.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = orderTableModel.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this order?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (DatabaseConnection.deleteOrder(orderId)) {
                    orderTableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Order deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete order.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.");
        }
    }
}
