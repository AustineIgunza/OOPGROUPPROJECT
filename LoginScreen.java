import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen extends JFrame {

    private JTextField usernameEmailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginScreen() {
        setTitle("BookNest Login");
        setSize(600, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Email (Customer) or Username (Admin):"));
        usernameEmailField = new JTextField();
        add(usernameEmailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> authenticateUser());
        add(new JLabel());
        add(loginButton);

        registerButton = new JButton("Register (Customer)");
        registerButton.addActionListener(e -> {
            new CustomerRegistration();
            dispose();
        });
        add(new JLabel());
        add(registerButton);

        setVisible(true);
    }

    private void authenticateUser() {
        String userInput = usernameEmailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (userInput.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // First check admin
            String adminQuery = "SELECT * FROM admins WHERE username = ? AND password = ?";
            PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
            adminStmt.setString(1, userInput);
            adminStmt.setString(2, password); // For production, hash & compare hashed passwords

            ResultSet adminRs = adminStmt.executeQuery();
            if (adminRs.next()) {
                // Admin logged in
                JOptionPane.showMessageDialog(this, "Welcome Admin!");
                dispose();
                new AdminDashboard(userInput).setVisible(true);
                return;
            }

            // Then check customer by email
            String custQuery = "SELECT * FROM customers WHERE email = ? AND password = ?";
            PreparedStatement custStmt = conn.prepareStatement(custQuery);
            custStmt.setString(1, userInput);
            custStmt.setString(2, password);

            ResultSet custRs = custStmt.executeQuery();
            if (custRs.next()) {
                String customerName = custRs.getString("name");
                JOptionPane.showMessageDialog(this, "Welcome " + customerName + "!");
                dispose();
                new CustomerDashboard(customerName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
