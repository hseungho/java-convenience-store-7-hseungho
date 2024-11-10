package store.core.model;

import java.util.Objects;
import store.commons.data.repository.Id;

public class ProductWindow {

    @Id
    private final String name;

    private final Product product;

    private final Product promotionProduct;

    public ProductWindow(String name, Product product, Product promotionProduct) {
        this.name = name;
        this.product = product;
        this.promotionProduct = promotionProduct;
    }

    public String getName() {
        return this.name;
    }

    public Product getProduct() {
        return this.product;
    }

    public Product getPromotionProduct() {
        return this.promotionProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductWindow that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
