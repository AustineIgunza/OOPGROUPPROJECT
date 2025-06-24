import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageCustomersFrame extends JFrame {

    private DefaultTableModel customerTableModel;
    private JTable customerTable;

    public ManageCustomersFrame() {
        setTitle("Manage Customers");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table columns
        String[] columns = {"Name", "Email", "Status"};
        customerTableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(customerTableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        // Buttons
        JButton addBtn = new JButton("Add Customer");
        JButton editBtn = new JButton("Edit Customer");
        JButton banBtn = new JButton("Ban Customer");

        addBtn.addActionListener(e -> showAddCustomerDialog());
        editBtn.addActionListener(e -> showEditCustomerDialog());
        banBtn.addActionListener(e -> banSelectedCustomer());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(banBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showAddCustomerDialog() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Email:", emailField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            customerTableModel.addRow(new Object[]{
                    nameField.getText(),
                    emailField.getText(),
                    "Active"
            });
        }
    }

    private void showEditCustomerDialog() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit.");
            return;
        }

        String name = (String) customerTableModel.getValueAt(selectedRow, 0);
        String email = (String) customerTableModel.getValueAt(selectedRow, 1);

        JTextField nameField = new JTextField(name);
        JTextField emailField = new JTextField(email);

        Object[] fields = {
                "Name:", nameField,
                "Email:", emailField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            customerTableModel.setValueAt(nameField.getText(), selectedRow, 0);
            customerTableModel.setValueAt(emailField.getText(), selectedRow, 1);
        }
    }

    private void banSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to ban this customer?",
                    "Confirm Ban", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                customerTableModel.setValueAt("Banned", selectedRow, 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to ban.");
        }
    }
}
