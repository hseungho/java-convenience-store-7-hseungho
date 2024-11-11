package store.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Payment {

    private final BigDecimal totalAmount;

    private final BigDecimal promotionDiscountAmount;

    private final BigDecimal membershipDiscountAmount;

    public Payment(BigDecimal totalAmount, BigDecimal promotionDiscountAmount, BigDecimal membershipDiscountAmount) {
        this.totalAmount = totalAmount;
        this.promotionDiscountAmount = promotionDiscountAmount;
        this.membershipDiscountAmount = membershipDiscountAmount;
    }

    public Payment(Order order) {
        this.totalAmount = order.getItems().stream()
                .map(it -> it.getProduct().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.promotionDiscountAmount = order.getPromotionItems().stream()
                .map(it -> it.getProduct().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promotionDiscountAmount = order.getItems().stream()
                .filter(OrderItem::isPromotion)
                .map(it -> it.getProduct().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.membershipDiscountAmount = totalAmount
                .subtract(promotionDiscountAmount)
                .multiply(BigDecimal.valueOf(0.3))
                .setScale(0, RoundingMode.DOWN)
                .min(BigDecimal.valueOf(8000));
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public BigDecimal getPromotionDiscountAmount() {
        return this.promotionDiscountAmount;
    }

    public BigDecimal getMembershipDiscountAmount() {
        return this.membershipDiscountAmount;
    }

    public BigDecimal geyPaidAmount() {
        return this.totalAmount
                .subtract(this.promotionDiscountAmount)
                .subtract(this.membershipDiscountAmount);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "totalAmount=" + getTotalAmount() +
                ", promotionDiscountAmount=" + getPromotionDiscountAmount() +
                ", membershipDiscountAmount=" + getMembershipDiscountAmount() +
                ", paidAmount=" + geyPaidAmount() +
                '}';
    }
}
