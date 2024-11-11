package store.core.model;

public class OrderItem {

    private final Product product;

    private final Long quantity;

    private final boolean isPromotion;

    public OrderItem(Product product, Long quantity, boolean isPromotion) {
        this.product = product;
        this.quantity = quantity;
        this.isPromotion = isPromotion;
    }

    public Product getProduct() {
        return this.product;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public boolean isPromotion() {
        return this.isPromotion;
    }
}
