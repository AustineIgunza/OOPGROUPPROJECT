import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void testCartItemCreation() {
        Book book = new Book(1, "Test Book", "Test Author", "Genre", "Category", 100.0, 10);
        CartItem item = new CartItem(book, 3);

        assertEquals(book, item.getBook());
        assertEquals(3, item.getQuantity());
    }

    @Test
    void testTotalPriceCalculation() {
        Book book = new Book(2, "Clean Code", "Robert Martin", "Programming", "Tech", 250.0, 5);
        CartItem item = new CartItem(book, 2);

        assertEquals(500.0, item.getTotalPrice());
    }

    @Test
    void testQuantityUpdate() {
        Book book = new Book(3, "Atomic Habits", "James Clear", "Self-help", "Motivation", 300.0, 5);
        CartItem item = new CartItem(book, 1);

        item.setQuantity(4);
        assertEquals(4, item.getQuantity());
        assertEquals(1200.0, item.getTotalPrice());
    }

    @Test
    void testToStringFormat() {
        Book book = new Book(4, "Rich Dad Poor Dad", "Kiyosaki", "Finance", "Personal", 150.0, 8);
        CartItem item = new CartItem(book, 2);

        String output = item.toString();
        assertTrue(output.contains("Rich Dad Poor Dad"));
        assertTrue(output.contains("x 2"));
        assertTrue(output.contains("$300.0"));
    }
}
