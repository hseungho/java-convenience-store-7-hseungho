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
        Long defaultProductQuantityIfRequired = quantity;
        boolean isPromotion = false;
        if ( productWindow.getPromotionProduct() != null &&  productWindow.getPromotionProduct().getQuantity() > 0) {
            Long targetPromotionQuantity = getTargetPromotionQuantity(productWindow, defaultProductQuantityIfRequired);
            Long decreaseQuantity = addPromotionItem(productWindow, targetPromotionQuantity,  productWindow.getPromotionProduct());
            defaultProductQuantityIfRequired -= decreaseQuantity;
            isPromotion = true;
        }
        this.items.add(new OrderItem(decreaseProductQuantityIfRequired(productWindow, defaultProductQuantityIfRequired), quantity, isPromotion));
    }

    private Long getTargetPromotionQuantity(ProductWindow productWindow, Long quantity) {
        Long targetPromotionQuantity = quantity;
        Long insufficientPromotionQuantity = productWindow.getInsufficientPromotionQuantityIfExceed(quantity, this.orderDate);
        if (insufficientPromotionQuantity > 0) {
            targetPromotionQuantity -= insufficientPromotionQuantity;
        }
        return targetPromotionQuantity;
    }

    private Long addPromotionItem(ProductWindow productWindow, Long targetPromotionQuantity, Product promotionProduct) {
        Long availablePromotionQuantity = productWindow.getAvailablePromotionQuantity(targetPromotionQuantity, this.orderDate);
        Promotion promotion = promotionProduct.getPromotion();
        Long decreaseQuantity = availablePromotionQuantity * promotion.getPromotionSets();
        if (availablePromotionQuantity > 0) {
            promotionProduct.decreaseQuantity(decreaseQuantity);
            this.promotionItems.add(new OrderItem(promotionProduct, availablePromotionQuantity, true));
        }
        return decreaseQuantity;
    }

    private Product decreaseProductQuantityIfRequired(ProductWindow productWindow, Long defaultProductQuantityIfRequired) {
        Product product = productWindow.getProduct();
        if (defaultProductQuantityIfRequired > 0 && product != null) {
            product.decreaseQuantity(defaultProductQuantityIfRequired);
        }
        return product;
    }

    public void applyMembership() {
        this.isApplyMembership = true;
    }
}
