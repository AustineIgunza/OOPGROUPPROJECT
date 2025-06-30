import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<CartItem> items = new ArrayList<>();

    public void addItem(Book book, int quantity) {
        if (book == null || quantity <= 0) return;

        for (CartItem item : items) {
            if (item.getBook().getBookId() == book.getBookId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(book, quantity));
    }

    public boolean updateQuantity(int bookId, int newQuantity) {
        if (newQuantity <= 0) return removeItem(bookId);

        for (CartItem item : items) {
            if (item.getBook().getBookId() == bookId) {
                item.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(int bookId) {
        return items.removeIf(item -> item.getBook().getBookId() == bookId);
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public String getCartSummary() {
        if (isEmpty()) return "Your cart is empty";

        StringBuilder summary = new StringBuilder("Your Shopping Cart:\n");
        items.forEach(item -> summary.append("- ").append(item).append("\n"));
        summary.append(String.format("Total: $%.2f", getTotalPrice()));
        return summary.toString();
    }

    public int getItemCount() {
        return items.size();
    }
}
