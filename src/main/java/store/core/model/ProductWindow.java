package store.core.model;

import java.time.LocalDate;
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

    public Long getRequiredPromotionQuantityIfApplicable(Long orderQuantity, LocalDate orderDate) {
        if (this.promotionProduct != null &&
                this.promotionProduct.hasPromotion() &&
                this.promotionProduct.getPromotion().isActiveAt(orderDate) &&
                Objects.equals(this.promotionProduct.getPromotion().getBuy(), orderQuantity)) {
            return this.promotionProduct.getPromotion().getGet();
        }
        return 0L;
    }

    public Long getInsufficientPromotionQuantityIfExceed(Long orderQuantity, LocalDate orderDate) {
        if (this.promotionProduct != null &&
                this.promotionProduct.hasPromotion() &&
                this.promotionProduct.getPromotion().isActiveAt(orderDate) &&
                this.promotionProduct.getQuantity() < orderQuantity) {
            Promotion promotion = this.promotionProduct.getPromotion();
            Long promotionSets = promotion.getPromotionSets();
            Long availableSets = this.promotionProduct.getQuantity() / promotionSets;
            Long availableQuantity = promotionSets * availableSets;
            if (availableQuantity == 0) return 0L;
            return orderQuantity - availableQuantity;
        }
        return 0L;
    }

    public Long getAvailablePromotionQuantity(Long orderQuantity, LocalDate orderDate) {
        if (this.promotionProduct == null ||
                this.promotionProduct.hasNotPromotion() ||
                this.promotionProduct.getPromotion().isInactiveAt(orderDate)) {
            return 0L;
        }
        Promotion promotion = this.promotionProduct.getPromotion();
        Long promotionSets = promotion.getPromotionSets();
        Long availableSets = orderQuantity / promotionSets;
        return availableSets / promotion.getGet();
    }

    public Long getAvailableQuantity() {
        if (this.product == null) {
            return 0L;
        }
        return this.product.getQuantity();
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
