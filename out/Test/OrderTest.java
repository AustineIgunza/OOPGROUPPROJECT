import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(1001); // customer ID
    }

    @Test
    void testAddOrderItemsAndTotalPrice() {
        order.addOrderItem(1, 2, 300.0); // 2 x 300 = 600
        order.addOrderItem(2, 1, 500.0); // 1 x 500 = 500

        assertEquals(2, order.getOrderItems().size());
        assertEquals(1100.0, order.getTotalPrice());
    }

    @Test
    void testInitialStatusIsPending() {
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testShipOrderChangesStatusToShipped() {
        order.shipOrder();
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void testCancelOrderWorksIfPending() {
        order.cancelOrder();
        assertEquals(Order.OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testCancelOrderDoesNotWorkIfAlreadyShipped() {
        order.shipOrder();         // Status becomes SHIPPED
        order.cancelOrder();       // Should not change
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void testToStringIncludesOrderSummary() {
        order.addOrderItem(3, 1, 150.0);
        String summary = order.toString();

        assertTrue(summary.contains("ORDER SUMMARY"));
        assertTrue(summary.contains("Book ID: 3"));
        assertTrue(summary.contains("Quantity: 1"));
        assertTrue(summary.contains("TOTAL: $150.00"));
    }
}
