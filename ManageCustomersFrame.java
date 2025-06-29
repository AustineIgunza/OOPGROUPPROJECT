import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageCustomersFrame extends JFrame {
    private DefaultTableModel customerTableModel;
    private JTable customerTable;

    public ManageCustomersFrame() {
        setTitle("👥 Manage Customers");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        Color backgroundColor = new Color(245, 245, 255);
        Color primaryColor = new Color(63, 81, 181);
        Font font = new Font("Segoe UI", Font.PLAIN, 15);

        getContentPane().setBackground(backgroundColor);

        String[] columns = {"Customer ID", "Name", "Email", "Status"};
        customerTableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(customerTableModel);
        customerTable.setFont(font);
        customerTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Customer List"));
        add(scrollPane, BorderLayout.CENTER);

        JButton banBtn = createStyledButton("🚫 Ban Customer", primaryColor, font);
        JButton unbanBtn = createStyledButton("✅ Unban Customer", primaryColor, font);

        banBtn.addActionListener(e -> changeStatus("Banned"));
        unbanBtn.addActionListener(e -> changeStatus("Active"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(banBtn);
        buttonPanel.add(unbanBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Sample customer
        customerTableModel.addRow(new Object[]{"CUST001", "Jane Doe", "jane@example.com", "Active"});

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

    private void changeStatus(String newStatus) {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            customerTableModel.setValueAt(newStatus, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Customer status updated to: " + newStatus);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer.");
        }
    }
}
