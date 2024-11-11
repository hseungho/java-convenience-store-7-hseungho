package store.core.model;

public class OrderItem {

    private final Product product;

    private final Long quantity;

    public OrderItem(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" + product.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
