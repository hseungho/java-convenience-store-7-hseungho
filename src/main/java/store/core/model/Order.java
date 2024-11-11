package store.core.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private final List<OrderItem> items;

    private final List<OrderItem> promotionItems;

    private final LocalDate orderDate;

    private boolean isApplyMembership;

    private Payment payment;

    private Receipt receipt;

    public Order(LocalDate orderDate) {
        this.items = new ArrayList<>();
        this.promotionItems = new ArrayList<>();
        this.orderDate = orderDate;
        this.isApplyMembership = false;
    }

    public List<OrderItem> getItems() {
        return this.items;
    }

    public LocalDate getOrderDate() {
        return this.orderDate;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public Receipt getReceipt() {
        return this.receipt;
    }

    public void addItem(ProductWindow productWindow, Long quantity) {
        Product promotionProduct = productWindow.getPromotionProduct();
        Long defaultProductQuantityIfRequired = quantity;
        if (promotionProduct != null) {
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
                this.promotionItems.add(new OrderItem(promotionProduct, availablePromotionQuantity));
            }
            defaultProductQuantityIfRequired -= decreaseQuantity;
        }
        Product product = productWindow.getProduct();
        if (defaultProductQuantityIfRequired > 0 && product != null) {
            product.decreaseQuantity(defaultProductQuantityIfRequired);
        }
        this.items.add(new OrderItem(product, quantity));
    }

    @Override
    public String toString() {
        return "Order{" +
                "items=" + items +
                ", promotionItems=" + promotionItems +
                ", orderDate=" + orderDate +
                ", payment=" + payment +
                ", receipt=" + receipt +
                '}';
    }
}
