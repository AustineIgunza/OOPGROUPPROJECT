public class SharedCart {
    private static final ShoppingCart cart = new ShoppingCart();

    public static ShoppingCart getCart() {
        return cart;
    }
}
