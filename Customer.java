public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String status;

    public Customer(int customerId, String name, String email, String status) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
