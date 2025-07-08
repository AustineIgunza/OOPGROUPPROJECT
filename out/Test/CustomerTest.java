import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        Customer customer = new Customer(1, "Austine", "austine@example.com", "Active");

        assertEquals(1, customer.getCustomerId());
        assertEquals("Austine", customer.getName());
        assertEquals("austine@example.com", customer.getEmail());
        assertEquals("Active", customer.getStatus());
    }

    @Test
    void testSetters() {
        Customer customer = new Customer(2, "Jane", "jane@example.com", "Inactive");

        customer.setCustomerId(3);
        customer.setName("Janet");
        customer.setEmail("janet@example.com");
        customer.setStatus("Active");

        assertEquals(3, customer.getCustomerId());
        assertEquals("Janet", customer.getName());
        assertEquals("janet@example.com", customer.getEmail());
        assertEquals("Active", customer.getStatus());
    }

    @Test
    void testChangeStatus() {
        Customer customer = new Customer(4, "Brian", "brian@example.com", "Inactive");
        customer.setStatus("Suspended");
        assertEquals("Suspended", customer.getStatus());
    }
}
