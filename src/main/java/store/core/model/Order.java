package store.core.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private final List<OrderItem> items;

    private final List<OrderItem> promotionItems;

    private final LocalDate orderDate;

    private boolean isApplyMembership;

    public Order(LocalDate orderDate) {
        this.items = new ArrayList<>();
        this.promotionItems = new ArrayList<>();
        this.orderDate = orderDate;
        this.isApplyMembership = false;
    }

    public List<OrderItem> getItems() {
        return this.items;
    }

    public List<OrderItem> getPromotionItems() {
        return this.promotionItems;
    }

    public LocalDate getOrderDate() {
        return this.orderDate;
    }

    public boolean isApplyMembership() {
        return this.isApplyMembership;
    }

    public Payment getPayment() {
        return new Payment(this);
    }

    public Receipt getReceipt() {
        return new Receipt(this.items, this.promotionItems, getPayment());
    }

    public void addItem(ProductWindow productWindow, Long quantity) {
        Product promotionProduct = productWindow.getPromotionProduct();
        Long defaultProductQuantityIfRequired = quantity;
        boolean isPromotion = false;
        if (promotionProduct != null && promotionProduct.getQuantity() > 0) {
            Long targetPromotionQuantity = quantity;
            Long insufficientPromotionQuantity = productWindow.getInsufficientPromotionQuantityIfExceed(quantity, this.orderDate);
            if (insufficientPromotionQuantity > 0) {
                targetPromotionQuantity -= insufficientPromotionQuantity;
            }

            Long availablePromotionQuantity = productWindow.getAvailablePromotionQuantity(targetPromotionQuantity, this.orderDate);
            Promotion promotion = promotionProduct.getPromotion();
            Long decreaseQuantity = availablePromotionQuantity * promotion.getPromotionSets();
            if (availablePromotionQuantity > 0) {
                promotionProduct.decreaseQuantity(decreaseQuantity);
                this.promotionItems.add(new OrderItem(promotionProduct, availablePromotionQuantity, true));
            }
            isPromotion = true;
            defaultProductQuantityIfRequired -= decreaseQuantity;
        }
        Product product = productWindow.getProduct();
        if (defaultProductQuantityIfRequired > 0 && product != null) {
            product.decreaseQuantity(defaultProductQuantityIfRequired);
        }
        this.items.add(new OrderItem(product, quantity, isPromotion));
    }

    public void applyMembership() {
        this.isApplyMembership = true;
    }
}
