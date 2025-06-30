import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageBooksFrame extends JFrame {
    private DefaultTableModel bookTableModel;
    private JTable bookTable;

    public ManageBooksFrame() {
        setTitle("📚 Manage Books");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        Font font = new Font("Segoe UI", Font.PLAIN, 15);
        Color primary = new Color(76, 175, 80);
        Color bg = new Color(240, 255, 240);
        getContentPane().setBackground(bg);

        String[] columns = {"ID", "Title", "Author", "Genre", "Category", "Price", "Stock"};
        bookTableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(bookTableModel);
        bookTable.setFont(font);
        bookTable.setRowHeight(22);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Book Inventory"));
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = createStyledButton("🔄 Refresh", primary, font);
        JButton addBtn = createStyledButton("➕ Add Book", primary, font);
        JButton deleteBtn = createStyledButton("🗑️ Delete Book", primary, font);

        refreshBtn.addActionListener(e -> loadBooks());
        addBtn.addActionListener(e -> addNewBook());
        deleteBtn.addActionListener(e -> deleteSelectedBook());

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(bg);
        panel.add(refreshBtn);
        panel.add(addBtn);
        panel.add(deleteBtn);
        add(panel, BorderLayout.SOUTH);

        loadBooks();
        setVisible(true);
    }

    private void loadBooks() {
        bookTableModel.setRowCount(0);
        List<Book> books = DatabaseConnection.getAllBooks();
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{
                    book.getBookId(), book.getTitle(), book.getAuthor(),
                    book.getGenre(), book.getCategory(), book.getPrice(), book.getStock()
            });
        }
    }

    private void addNewBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Title:")); panel.add(titleField);
        panel.add(new JLabel("Author:")); panel.add(authorField);
        panel.add(new JLabel("Genre:")); panel.add(genreField);
        panel.add(new JLabel("Category:")); panel.add(categoryField);
        panel.add(new JLabel("Price:")); panel.add(priceField);
        panel.add(new JLabel("Stock:")); panel.add(stockField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String genre = genreField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                Book book = new Book(0, title, author, genre, category, price, stock);
                if (DatabaseConnection.addBook(book)) {
                    JOptionPane.showMessageDialog(this, "Book added successfully.");
                    loadBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add book.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number input.");
            }
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            int bookId = Integer.parseInt(bookTableModel.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (DatabaseConnection.deleteBookById(bookId)) {
                    bookTableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Book deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete book.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book.");
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
