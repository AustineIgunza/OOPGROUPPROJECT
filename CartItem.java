public class CartItem {
    private Book book;
    private int quantity;

    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    // Getters and setters
    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return book.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return book.toString() + " x " + quantity + " = $" + getTotalPrice();
    }
}
