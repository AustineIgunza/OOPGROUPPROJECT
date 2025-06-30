import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private String paymentId;
    private int orderId;
    private double amount;
    private String paymentMethod;
    private boolean isSuccessful;
    private LocalDateTime paymentTime;

    public Payment(int orderId, double amount, String paymentMethod) {
        this.paymentId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentTime = LocalDateTime.now();
        this.isSuccessful = false;
    }

    public void processPayment() {
        System.out.println("Processing payment...");
        this.isSuccessful = true;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }
}
