public class CartContext {
    private static ShoppingCart cart = new ShoppingCart();
    private static int customerId;

    public static ShoppingCart getCart() {
        return cart;
    }

    public static void clearCart() {
        cart.clear();
    }

    public static int getCustomerId() {
        return customerId;
    }

    public static void setCustomerId(int id) {
        customerId = id;
    }
}
