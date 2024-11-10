package store.core.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import store.commons.data.repository.Id;

public class ProductWindow {

    @Id
    private final String name;

    private final List<Product> products;

    public ProductWindow(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return this.name;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public Optional<Product> findPromotionProduct() {
        return this.getProducts().stream()
                .filter(Product::hasPromotion)
                .findFirst();
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
