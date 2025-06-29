import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageBooksFrame extends JFrame {
    private DefaultTableModel bookTableModel;
    private JTable bookTable;

    public ManageBooksFrame() {
        setTitle("Manage Books");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Columns: ISBN, Title, Author, Price
        String[] columns = {"ISBN", "Title", "Author", "Price"};
        bookTableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Buttons
        JButton addBtn = new JButton("Add Book");
        JButton editBtn = new JButton("Edit Book");
        JButton deleteBtn = new JButton("Delete Book");

        addBtn.addActionListener(e -> showAddBookDialog());
        editBtn.addActionListener(e -> showEditBookDialog());
        deleteBtn.addActionListener(e -> deleteSelectedBook());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showAddBookDialog() {
        JTextField isbnField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField priceField = new JTextField();

        Object[] fields = {
                "ISBN:", isbnField,
                "Title:", titleField,
                "Author:", authorField,
                "Price:", priceField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            bookTableModel.addRow(new Object[]{
                    isbnField.getText(),
                    titleField.getText(),
                    authorField.getText(),
                    priceField.getText()
            });
        }
    }

    private void showEditBookDialog() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.");
            return;
        }

        String isbn = (String) bookTableModel.getValueAt(selectedRow, 0);
        String title = (String) bookTableModel.getValueAt(selectedRow, 1);
        String author = (String) bookTableModel.getValueAt(selectedRow, 2);
        String price = (String) bookTableModel.getValueAt(selectedRow, 3);

        JTextField isbnField = new JTextField(isbn);
        JTextField titleField = new JTextField(title);
        JTextField authorField = new JTextField(author);
        JTextField priceField = new JTextField(price);

        Object[] fields = {
                "ISBN:", isbnField,
                "Title:", titleField,
                "Author:", authorField,
                "Price:", priceField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            bookTableModel.setValueAt(isbnField.getText(), selectedRow, 0);
            bookTableModel.setValueAt(titleField.getText(), selectedRow, 1);
            bookTableModel.setValueAt(authorField.getText(), selectedRow, 2);
            bookTableModel.setValueAt(priceField.getText(), selectedRow, 3);
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
        }
    }
}