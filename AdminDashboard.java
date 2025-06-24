import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboard extends JFrame {

    private final String username;

    public AdminDashboard(String username) {
        this.username = username;

        setTitle("Admin Panel - Welcome " + username);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(63, 81, 181));  // Indigo background

        // Clean welcome label
        JLabel welcomeLabel = new JLabel("Welcome, Admin " + username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(welcomeLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 50, 200));
        buttonPanel.setBackground(new Color(63, 81, 181));

        String[] buttonLabels = {"Manage Books", "Manage Customers", "Manage Orders", "Logout"};

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(63, 81, 181));
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

            // Hover effect
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(230, 230, 250)); // light lavender
                }

                public void mouseExited(MouseEvent e) {
                    button.setBackground(Color.WHITE);
                }
            });

            button.addActionListener(e -> handleButtonClick(label));
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void handleButtonClick(String label) {
        switch (label) {
            case "Manage Books" -> new ManageBooksFrame();
            case "Manage Customers" -> new ManageCustomersFrame();
            case "Manage Orders" -> new ManageOrdersFrame();
            case "Logout" -> {
                dispose();
                new LoginScreen().setVisible(true);
            }
        }
    }

    // Simple stubs for demonstration
    static class ManageBooksFrame extends JFrame {
        public ManageBooksFrame() {
            setTitle("Manage Books");
            setSize(500, 400);
            setLocationRelativeTo(null);
            add(new JLabel("Book Management Panel (to be implemented)", JLabel.CENTER));
            setVisible(true);
            new ManageBooksFrame().setVisible(true);
        }
    }

    static class ManageCustomersFrame extends JFrame {
        public ManageCustomersFrame() {
            setTitle("Manage Customers");
            setSize(500, 400);
            setLocationRelativeTo(null);
            add(new JLabel("Customer Management Panel (to be implemented)", JLabel.CENTER));
            setVisible(true);
        }
    }

    static class ManageOrdersFrame extends JFrame {
        public ManageOrdersFrame() {
            setTitle("Manage Orders");
            setSize(500, 400);
            setLocationRelativeTo(null);
            add(new JLabel("Order Management Panel (to be implemented)", JLabel.CENTER));
            setVisible(true);
        }
    }
}
