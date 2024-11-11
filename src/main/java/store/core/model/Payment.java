package store.core.model;

import java.math.BigDecimal;

public class Payment {

    private final BigDecimal totalAmount;

    private final BigDecimal promotionDiscountAmount;

    private final BigDecimal membershipDiscountAmount;

    public Payment(BigDecimal totalAmount, BigDecimal promotionDiscountAmount, BigDecimal membershipDiscountAmount) {
        this.totalAmount = totalAmount;
        this.promotionDiscountAmount = promotionDiscountAmount;
        this.membershipDiscountAmount = membershipDiscountAmount;
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
                "totalAmount=" + totalAmount +
                ", promotionDiscountAmount=" + promotionDiscountAmount +
                ", membershipDiscountAmount=" + membershipDiscountAmount +
                '}';
    }
}
