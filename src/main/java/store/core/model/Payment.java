package store.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Payment {

    private final BigDecimal totalAmount;

    private final BigDecimal promotionDiscountAmount;

    private final BigDecimal membershipDiscountAmount;

    public Payment(BigDecimal totalAmount,
                   BigDecimal promotionDiscountAmount,
                   BigDecimal membershipDiscountAmount) {
        this.totalAmount = totalAmount;
        this.promotionDiscountAmount = promotionDiscountAmount;
        this.membershipDiscountAmount = membershipDiscountAmount;
    }

    public Payment(Order order) {
        this.totalAmount = calcTotalAmount(order);
        this.promotionDiscountAmount = calcPromotionDiscountAmount(order);
        this.membershipDiscountAmount = calcMembershipDiscountAmount(order, totalAmount);
    }

    private static BigDecimal calcTotalAmount(Order order) {
        return order.getItems().stream()
                .map(it -> it.getProduct().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calcPromotionDiscountAmount(Order order) {
        return order.getPromotionItems().stream()
                .map(it -> it.getProduct().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calcMembershipDiscountAmount(Order order, BigDecimal totalAmount) {
        BigDecimal membershipDiscountAmount = BigDecimal.ZERO;
        if (order.isApplyMembership()) {
            membershipDiscountAmount = totalAmount
                    .subtract(calcPromotionAmount(order))
                    .multiply(BigDecimal.valueOf(0.3))
                    .setScale(0, RoundingMode.DOWN)
                    .min(BigDecimal.valueOf(8000));
        }
        return membershipDiscountAmount;
    }

    private static BigDecimal calcPromotionAmount(Order order) {
        return order.getItems().stream()
                .filter(OrderItem::isPromotion)
                .map(it -> it.getProduct().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
}
