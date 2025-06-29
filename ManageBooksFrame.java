import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageBooksFrame extends JFrame {
    private DefaultTableModel bookTableModel;
    private JTable bookTable;

    public ManageBooksFrame() {
        setTitle("📚 Manage Books");
        setSize(850, 450);
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
        refreshBtn.addActionListener(e -> loadBooks());

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(bg);
        panel.add(refreshBtn);
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
