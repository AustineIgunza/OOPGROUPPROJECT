import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookCreationAndGetters() {
        Book book = new Book(101, "Clean Code", "Robert C. Martin", "Programming", "Technical", 1500.0, 10);

        assertEquals(101, book.getBookId());
        assertEquals("Clean Code", book.getTitle());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals("Programming", book.getGenre());
        assertEquals("Technical", book.getCategory());
        assertEquals(1500.0, book.getPrice());
        assertEquals(10, book.getStock());
    }

    @Test
    void testIsInStockTrue() {
        Book book = new Book(102, "The Pragmatic Programmer", "Andy Hunt", "Software", "Technical", 1200.0, 5);
        assertTrue(book.isInStock());
    }

    @Test
    void testIsInStockFalse() {
        Book book = new Book(103, "Out of Stock Book", "Unknown", "Fiction", "Drama", 500.0, 0);
        assertFalse(book.isInStock());
    }

    @Test
    void testReduceStock() {
        Book book = new Book(104, "Refactoring", "Martin Fowler", "Programming", "Technical", 1100.0, 5);
        book.reduceStock(2);
        assertEquals(3, book.getStock());

        book.reduceStock(3);
        assertEquals(0, book.getStock());
    }

    @Test
    void testReduceStockTooMuch() {
        Book book = new Book(105, "Test Book", "Test Author", "Genre", "Category", 700.0, 2);
        book.reduceStock(5); // Too much
        assertEquals(2, book.getStock()); // Should not reduce
    }
}
