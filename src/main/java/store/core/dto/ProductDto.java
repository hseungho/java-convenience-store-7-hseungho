package store.core.dto;

import java.math.BigDecimal;
import store.core.model.Product;

public record ProductDto(
        String name,
        Long quantity,
        BigDecimal price,
        String promotionName
) {

    public static ProductDto modelOf(Product product) {
        String promotionName = "";
        if (product.getPromotion() != null) {
            promotionName = product.getPromotion().getName();
        }
        return new ProductDto(
                product.getName(),
                product.getQuantity(),
                product.getPrice(),
                promotionName
        );
    }
}
