import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testPaymentInitialization() {
        Payment payment = new Payment(2001, 1500.0, "Mpesa");

        assertNotNull(payment.getPaymentId());
        assertEquals(2001, payment.getOrderId());
        assertEquals(1500.0, payment.getAmount());
        assertEquals("Mpesa", payment.getPaymentMethod());
        assertFalse(payment.isSuccessful());
        assertNotNull(payment.getPaymentTime());
    }

    @Test
    void testProcessPayment() {
        Payment payment = new Payment(2002, 2000.0, "Card");

        payment.processPayment();

        assertTrue(payment.isSuccessful());
    }
}
