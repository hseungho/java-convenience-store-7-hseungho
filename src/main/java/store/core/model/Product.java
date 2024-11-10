package store.core.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import store.commons.data.repository.GenerateValue;
import store.commons.data.repository.Id;

public class Product {

    @Id
    @GenerateValue
    private Long id;

    private final String name;

    private final BigDecimal price;

    private Long quantity;

    private Optional<Promotion> promotion;

    public Product(Long id, String name, BigDecimal price, Long quantity, Optional<Promotion> promotion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public Product(String name, BigDecimal price, Long quantity, Optional<Promotion> promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.quantity = 0L;
        this.promotion = Optional.empty();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public Optional<Promotion> getPromotion() {
        return this.promotion;
    }

    public boolean hasPromotion() {
        return this.promotion.isPresent();
    }

    public boolean hasNotPromotion() {
        return !this.hasPromotion();
    }

    public void decreaseQuantity(Long orderQuantity) {
        if (orderQuantity > this.quantity) {
            throw new IllegalArgumentException("[ERROR] 재고가 부족합니다.");
        }
        this.quantity -= orderQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
