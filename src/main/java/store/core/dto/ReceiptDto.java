package store.core.dto;

import java.math.BigDecimal;
import java.util.List;
import store.core.model.OrderItem;
import store.core.model.Payment;
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
        Long totalQuantity = calcTotalQuantity(receipt.getPurchasedItems());
        List<ReceiptPurchaseItemDto> purchaseItems = mapToReceiptPurchaseItems(receipt.getPurchasedItems());
        List<ReceiptPromotionItemDto> promotionItems = mapToReceiptPromotionItems(receipt.getPromotionItems());
        ReceiptPaymentDto payment = mapToReceiptPaymentDto(totalQuantity, receipt.getPayment());
        return new ReceiptDto(purchaseItems, promotionItems, payment);
    }

    private static Long calcTotalQuantity(List<OrderItem> purchaseItems) {
        return purchaseItems.stream()
                .mapToLong(OrderItem::getQuantity)
                .sum();
    }

    private static List<ReceiptPurchaseItemDto> mapToReceiptPurchaseItems(List<OrderItem> purchaseItems) {
        return purchaseItems.stream()
                .map(it -> new ReceiptPurchaseItemDto(it.getProduct().getName(), it.getQuantity(), it.getProduct().getPrice()))
                .toList();
    }

    private static List<ReceiptPromotionItemDto> mapToReceiptPromotionItems(List<OrderItem> promotionItems) {
        return promotionItems.stream()
                .map(it -> new ReceiptPromotionItemDto(it.getProduct().getName(), it.getQuantity()))
                .toList();
    }

    private static ReceiptPaymentDto mapToReceiptPaymentDto(Long totalQuantity, Payment payment) {
        return new ReceiptPaymentDto(totalQuantity, payment.getTotalAmount(), payment.getPromotionDiscountAmount(), payment.getMembershipDiscountAmount(), payment.geyPaidAmount());
    }
}
