import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("BookNest - Login");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 240, 255));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));


        JLabel titleLabel = new JLabel("📚 BookNest");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(80, 0, 130));
        add(Box.createVerticalStrut(20));
        add(titleLabel);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(300, 35));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        add(Box.createVerticalStrut(20));
        add(usernameField);

        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"User", "Admin"});
        roleCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roleCombo.setMaximumSize(new Dimension(300, 50));
        roleCombo.setBorder(BorderFactory.createTitledBorder("Login as"));
        add(Box.createVerticalStrut(10));
        add(roleCombo);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(new Color(102, 0, 204));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(120, 40));
        add(Box.createVerticalStrut(20));
        add(loginBtn);

        loginBtn.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your username.");
                return;
            }

            dispose(); // Close login window

            if (role.equalsIgnoreCase("Admin")) {
                // This will open your existing AdminDashboard class (not inner class)
                new AdminDashboard(username);
            } else {
                new CustomerDashboard(username);
            }
        });

        setVisible(true);
    }

    static class CustomerDashboard extends JFrame {
        public CustomerDashboard(String username) {
            setTitle("Browse Books - Welcome " + username);
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            getContentPane().setBackground(new Color(147, 112, 219));


            JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
            add(welcomeLabel, BorderLayout.NORTH);

            JTextArea bookList = new JTextArea();
            bookList.setText("📚 Available Books:\n" +
                    "1. Introduction to Java\n" +
                    "2. Object-Oriented Programming\n" +
                    "3. Data Structures and Algorithms\n" +
                    "4. Web Development with HTML/CSS/JS\n" +
                    "5. Database Systems");
            bookList.setEditable(false);
            bookList.setFont(new Font("Monospaced", Font.PLAIN, 14));
            add(new JScrollPane(bookList), BorderLayout.CENTER);

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}

