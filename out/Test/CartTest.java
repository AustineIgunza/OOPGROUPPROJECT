import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setup() {
        order = new Order(101);  // customer ID
    }

    @Test
    void testAddOrderItemsAndTotalPrice() {
        order.addOrderItem(1, 2, 300.0); // Book ID 1, Quantity 2, Price 300
        order.addOrderItem(2, 1, 500.0); // Book ID 2, Quantity 1, Price 500

        assertEquals(2, order.getOrderItems().size());
        assertEquals(1100.0, order.getTotalPrice());
    }

    @Test
    void testInitialStatus() {
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testShipOrderFromPending() {
        order.shipOrder();
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void testCancelOrderFromPending() {
        order.cancelOrder();
        assertEquals(Order.OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testCancelOrderFailsIfShipped() {
        order.shipOrder();  // move to SHIPPED
        order.cancelOrder(); // should NOT change
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void testToStringIncludesDetails() {
        order.addOrderItem(3, 1, 100.0);
        String summary = order.toString();

        assertTrue(summary.contains("ORDER SUMMARY"));
        assertTrue(summary.contains("Book ID: 3"));
        assertTrue(summary.contains("TOTAL: $100.00"));
    }
}
