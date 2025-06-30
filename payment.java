import java.time.LocalDateTime;
import java.util.UUID;

public class payment {

    private String paymentId;
    private String orderId;


    private double amount;
    private String paymentMethod;
    private boolean isSuccessful;
    private LocalDateTime paymentTime;

    // Constructor
    public payment(String orderId, double amount, String paymentMethod) {
        this.paymentId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentTime = LocalDateTime.now();
        this.isSuccessful = false;
    }

    // Simulate payment processing
    public void processPayment() {
        System.out.println("Initiating payment...");
        System.out.println("Order ID: " + orderId);
        System.out.println("Amount: " + amount);
        System.out.println("Payment Method: " + paymentMethod);
        // Simulated successful transaction
        this.isSuccessful = true;
        System.out.println("Payment Successful! Transaction ID: " + paymentId);
    }

    // Getters
    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
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

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", isSuccessful=" + isSuccessful +
                ", paymentTime=" + paymentTime +
                '}';
    }
}

