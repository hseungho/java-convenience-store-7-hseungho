package store.core.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import store.core.dto.ReceiptDto;
import store.core.dto.ReceiptDto.ReceiptPaymentDto;
import store.core.dto.ReceiptDto.ReceiptPromotionItemDto;
import store.core.dto.ReceiptDto.ReceiptPurchaseItemDto;

public class ReceiptOutputView implements OutputView<ReceiptDto> {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    public void display(ReceiptDto content) {
        displayPurchaseItems(content.purchaseItems());
        displayPromotionItems(content.promotionItems());
        displayPayment(content.payment());
    }

    private void displayPurchaseItems(List<ReceiptPurchaseItemDto> purchaseItems) {
        StringBuilder builder = new StringBuilder("\n============== W 편의점 ================\n");
        builder.append(String.format("%-12s %6s %15s%n", "상품명", "수량", "금액"));

        for (ReceiptPurchaseItemDto purchaseItem : purchaseItems) {
            BigDecimal price = purchaseItem.price().multiply(BigDecimal.valueOf(purchaseItem.quantity()));
            builder.append(String.format("%-12s %6d %15s%n",
                    purchaseItem.name(),
                    purchaseItem.quantity(),
                    decimalFormat.format(price)));
        }
        System.out.print(builder);
    }

    private void displayPromotionItems(List<ReceiptPromotionItemDto> promotionItems) {
        StringBuilder builder = new StringBuilder("=============증\t\t정===============\n");
        for (ReceiptPromotionItemDto promotionItem : promotionItems) {
            builder.append(String.format("%-12s %6d%n",
                    promotionItem.name(),
                    promotionItem.quantity()));
        }
        System.out.print(builder);
    }

    private void displayPayment(ReceiptPaymentDto payment) {
        StringBuilder builder = new StringBuilder("====================================\n");

        builder.append(String.format("%-12s %6d %15s%n", "총구매액", payment.totalPurchaseQuantity(), decimalFormat.format(payment.totalAmount())));
        builder.append(String.format("%-12s %21s%n", "행사할인", "-" + decimalFormat.format(payment.promotionDiscountAmount())));
        builder.append(String.format("%-12s %21s%n", "멤버십할인", "-" + decimalFormat.format(payment.membershipDiscountAmount())));
        builder.append(String.format("%-12s %21s%n", "내실돈", decimalFormat.format(payment.paidAmount())));

        builder.append("====================================\n\n");
        System.out.print(builder);
    }
}
