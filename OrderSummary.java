public class OrderSummary {
    private String orderId;
    private String customerName;
    private String bookTitle;
    private int quantity;
    private String status;

    public OrderSummary(String orderId, String customerName, String bookTitle, int quantity, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}
