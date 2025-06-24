import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        // Table setup
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

        updateBtn.addActionListener(e -> updateOrderStatus());
        deleteBtn.addActionListener(e -> deleteSelectedOrder());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Sample order
        orderTableModel.addRow(new Object[]{"ORD1001", "Jane Doe", "Clean Code", "2", "Pending"});

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

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
        return button;
    }

    private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            orderTableModel.setValueAt("Shipped", selectedRow, 4);
            JOptionPane.showMessageDialog(this, "Order marked as Shipped.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this order?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                orderTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.");
        }
    }
}
