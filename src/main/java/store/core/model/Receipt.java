package store.core.model;

import java.util.List;

public class Receipt {

    private final List<OrderItem> purchasedItems;

    private final List<OrderItem> promotionItems;

    private final Payment payment;

    public Receipt(List<OrderItem> purchasedItems, List<OrderItem> promotionItems, Payment payment) {
        this.purchasedItems = purchasedItems;
        this.promotionItems = promotionItems;
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "purchasedItems=" + purchasedItems +
                ", promotionItems=" + promotionItems +
                ", payment=" + payment +
                '}';
    }
}
