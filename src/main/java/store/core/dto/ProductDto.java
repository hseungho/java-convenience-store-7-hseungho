package store.core.dto;

import java.math.BigDecimal;
import store.core.model.Product;
import store.core.model.Promotion;

public record ProductDto(
        String name,
        Long quantity,
        BigDecimal price,
        String promotionName
) {

    public static ProductDto modelOf(Product product) {
        return new ProductDto(
                product.getName(),
                product.getQuantity(),
                product.getPrice(),
                product.getPromotion().map(Promotion::getName).orElse("")
        );
    }
}
