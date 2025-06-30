import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class BookSearchPanel extends JPanel {

    private JTextField searchField;
    private JTextArea resultArea;

    public BookSearchPanel() {
        setLayout(new BorderLayout(10, 10));

        // 🔍 Top: Search Input
        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");

        topPanel.add(new JLabel("Search by Title, Author, or Genre:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // 📝 Center: Result Output
        resultArea = new JTextArea(15, 60);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 🎯 Action: Perform Search
        searchButton.addActionListener(this::performSearch);
    }

    private void performSearch(ActionEvent e) {
        String value = searchField.getText().trim();

        if (value.isEmpty()) {
            resultArea.setText("⚠️ Please enter a search term.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT title, author, genre, price FROM books WHERE " +
                    "title LIKE ? OR author LIKE ? OR genre LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String wildcardValue = "%" + value + "%";
            stmt.setString(1, wildcardValue);
            stmt.setString(2, wildcardValue);
            stmt.setString(3, wildcardValue);

            ResultSet rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("📘 Title: ").append(rs.getString("title")).append("\n")
                        .append("✍️ Author: ").append(rs.getString("author")).append("\n")
                        .append("🎭 Genre: ").append(rs.getString("genre")).append("\n")
                        .append("💰 Price: Ksh ").append(rs.getDouble("price")).append("\n")
                        .append("-----------------------------------------------------\n");
            }

            resultArea.setText(sb.length() > 0 ? sb.toString() : "🚫 No matching books found.");

        } catch (SQLException ex) {
            resultArea.setText("❌ Database error: " + ex.getMessage());
        }
    }

    // For standalone testing (optional)
    public static void main(String[] args) {
        JFrame frame = new JFrame("Book Search Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BookSearchPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
