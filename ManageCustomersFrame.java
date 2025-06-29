import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageCustomersFrame extends JFrame {
    private DefaultTableModel customerTableModel;
    private JTable customerTable;

    public ManageCustomersFrame() {
        setTitle("👥 Manage Customers");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        Font font = new Font("Segoe UI", Font.PLAIN, 15);
        Color primaryColor = new Color(33, 150, 243);
        Color bg = new Color(245, 250, 255);
        getContentPane().setBackground(bg);

        String[] columns = {"Customer ID", "Name", "Email", "Status"};
        customerTableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(customerTableModel);
        customerTable.setFont(font);
        customerTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Customer List"));
        add(scrollPane, BorderLayout.CENTER);

        JButton banBtn = createStyledButton("🚫 Ban Customer", primaryColor, font);
        banBtn.addActionListener(e -> banCustomer());

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(bg);
        bottom.add(banBtn);
        add(bottom, BorderLayout.SOUTH);

        loadCustomers();
        setVisible(true);
    }

    private void loadCustomers() {
        customerTableModel.setRowCount(0);
        List<Customer> customers = DatabaseConnection.getAllCustomers();
        for (Customer c : customers) {
            customerTableModel.addRow(new Object[]{
                    c.getCustomerId(), c.getName(), c.getEmail(), c.getStatus()
            });
        }
    }

    private void banCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = Integer.parseInt(customerTableModel.getValueAt(selectedRow, 0).toString());
            boolean success = DatabaseConnection.updateCustomerStatus(customerId, "Banned");
            if (success) {
                customerTableModel.setValueAt("Banned", selectedRow, 3);
                JOptionPane.showMessageDialog(this, "Customer banned successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to ban customer.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer.");
        }
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
}
