package store.core.model;

import java.math.BigDecimal;
import java.util.Objects;
import store.commons.data.repository.GenerateValue;
import store.commons.data.repository.Id;

public class Product {

    @Id
    @GenerateValue
    private Long id;

    private final String name;

    private final BigDecimal price;

    private Long quantity;

    private Promotion promotion;

    public Product(Long id, String name, BigDecimal price, Long quantity, Promotion promotion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public Product(String name, BigDecimal price, Long quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.quantity = 0L;
        this.promotion = null;
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

    public Promotion getPromotion() {
        return this.promotion;
    }

    public boolean hasPromotion() {
        return this.promotion != null;
    }

    public boolean hasNotPromotion() {
        return this.promotion == null;
    }

    public void increaseQuantity(Long quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(Long orderQuantity) {
        if (orderQuantity > this.getQuantity()) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
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
