package store.core.dto;

import java.math.BigDecimal;
import java.util.List;
import store.core.model.OrderItem;
import store.core.model.Receipt;

public record ReceiptDto(
        List<ReceiptPurchaseItemDto> purchaseItems,
        List<ReceiptPromotionItemDto> promotionItems,
        ReceiptPaymentDto payment
) {

    public record ReceiptPurchaseItemDto(
            String name,
            Long quantity,
            BigDecimal price
    ) {}

    public record ReceiptPromotionItemDto(
            String name,
            Long quantity
    ) {}

    public record ReceiptPaymentDto(
            Long totalPurchaseQuantity,
            BigDecimal totalAmount,
            BigDecimal promotionDiscountAmount,
            BigDecimal membershipDiscountAmount,
            BigDecimal paidAmount
    ) {}

    public static ReceiptDto modelOf(Receipt receipt) {
        Long totalQuantity = receipt.getPurchasedItems().stream()
                .mapToLong(OrderItem::getQuantity)
                .sum();
        List<ReceiptPurchaseItemDto> purchaseItems = receipt.getPurchasedItems().stream()
                .map(it -> new ReceiptPurchaseItemDto(it.getProduct().getName(), it.getQuantity(), it.getProduct().getPrice()))
                .toList();
        List<ReceiptPromotionItemDto> promotionItems = receipt.getPromotionItems().stream()
                .map(it -> new ReceiptPromotionItemDto(it.getProduct().getName(), it.getQuantity()))
                .toList();
        ReceiptPaymentDto payment = new ReceiptPaymentDto(totalQuantity, receipt.getPayment().getTotalAmount(), receipt.getPayment().getPromotionDiscountAmount(), receipt.getPayment().getMembershipDiscountAmount(), receipt.getPayment().geyPaidAmount());
        return new ReceiptDto(purchaseItems, promotionItems, payment);
    }
}
