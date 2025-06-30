import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {



    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED;

        public static OrderStatus fromString(String status) {
            return OrderStatus.valueOf(status.toUpperCase());
        }
    }

    private int orderId;
    private int customerId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItem> orderItems;

    public Order(int customerId) {
        this.customerId = customerId;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.orderItems = new ArrayList<>();
    }

    // Getters & Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getCustomerId() { return customerId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    public void addOrderItem(int bookId, int quantity, double price) {
        orderItems.add(new OrderItem(bookId, quantity, price));
    }

    public double getTotalPrice() {
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // Save to DB
    public boolean saveOrder() {
        String insertOrderSQL = "INSERT INTO orders (customer_id, order_date, status) VALUES (?, ?, ?)";
        String insertItemSQL = "INSERT INTO order_items (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, customerId);
                psOrder.setTimestamp(2, Timestamp.valueOf(orderDate));
                psOrder.setString(3, status.name());

                int affected = psOrder.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    System.err.println("Failed to insert order.");
                    return false;
                }

                try (ResultSet keys = psOrder.getGeneratedKeys()) {
                    if (keys.next()) {
                        this.orderId = keys.getInt(1);
                    } else {
                        conn.rollback();
                        System.err.println("No order ID generated.");
                        return false;
                    }
                }
            }

            try (PreparedStatement psItem = conn.prepareStatement(insertItemSQL)) {
                for (OrderItem item : orderItems) {
                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.getBookId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getPrice());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
            System.out.println("Order saved: ID = " + orderId);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void shipOrder() {
        if (status == OrderStatus.PENDING || status == OrderStatus.PROCESSING) {
            status = OrderStatus.SHIPPED;
        }
    }

    public void cancelOrder() {
        if (status == OrderStatus.PENDING) {
            status = OrderStatus.CANCELLED;
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("================ ORDER SUMMARY ================\n");
        sb.append(String.format("ID: %d     Status: %s\n", orderId, status));
        sb.append(String.format("Date: %s\n", orderDate.format(formatter)));
        sb.append(String.format("Customer ID: %d\n", customerId));
        sb.append("---------------------------------------------\n");
        sb.append("Books:\n");
        for (OrderItem item : orderItems) {
            sb.append(String.format(" - Book ID: %d | Quantity: %d | Price: $%.2f\n",
                    item.getBookId(), item.getQuantity(), item.getPrice()));
        }
        sb.append("---------------------------------------------\n");
        sb.append(String.format("TOTAL: $%.2f\n", getTotalPrice()));
        sb.append("=============================================\n");
        return sb.toString();
    }

    public static class OrderItem {
        private final int bookId;
        private final int quantity;
        private final double price;

        public OrderItem(int bookId, int quantity, double price) {
            this.bookId = bookId;
            this.quantity = quantity;
            this.price = price;
        }

        public int getBookId() { return bookId; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
    }
}
