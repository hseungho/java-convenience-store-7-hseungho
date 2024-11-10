package store.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void create_should_be_pass() {
        // given
        // when
        String name = "product name";
        BigDecimal price = BigDecimal.valueOf(10000);
        Long quantity = 10L;
        Promotion promotion = new Promotion("promotion", 1, 1, LocalDate.now(), LocalDate.now());
        Product product = new Product(name, price, quantity, Optional.of(promotion));
        // then
        Assertions.assertNotNull(product);
        Assertions.assertEquals(name, product.getName());
        Assertions.assertEquals(price, product.getPrice());
        Assertions.assertEquals(quantity, product.getQuantity());
        Assertions.assertEquals(promotion, product.getPromotion().get());
    }

    @Test
    void equals_should_be_pass() {
        // given
        BigDecimal price = BigDecimal.valueOf(10000);
        Long quantity = 100L;
        Promotion promotion = new Promotion("promotion", 1, 1, LocalDate.now(), LocalDate.now());
        // when
        Product product1 = new Product(1L, "product", price, quantity, Optional.of(promotion));
        Product product2 = new Product(1L, "product", price, quantity, Optional.empty());
        Product product3 = new Product(2L, "other_product", price, quantity, Optional.of(promotion));
        // then
        Assertions.assertEquals(product1, product2);
        Assertions.assertNotEquals(product1, product3);
        Assertions.assertNotEquals(product2, product3);
    }

    @Test
    void hasPromotion_should_be_pass() {
        // given
        BigDecimal price = BigDecimal.valueOf(10000);
        Long quantity = 100L;
        Promotion promotion = new Promotion("promotion", 1, 1, LocalDate.now(), LocalDate.now());
        Product productHasPromotion = new Product("product", price, quantity, Optional.of(promotion));
        Product productHasNotPromotion = new Product("other_product", price, quantity, Optional.empty());
        // when
        boolean hasPromotion = productHasPromotion.hasPromotion();
        boolean hasNotPromotion = productHasNotPromotion.hasPromotion();
        // then
        Assertions.assertTrue(hasPromotion);
        Assertions.assertFalse(hasNotPromotion);
    }

    @Test
    void decreaseQuantity_should_be_pass() {
        // given
        BigDecimal price = BigDecimal.valueOf(10000);
        Long quantity = 10L;
        Product product = new Product(1L, "product", price, quantity, Optional.empty());
        // when
        product.decreaseQuantity(10L);
        // then
        Assertions.assertEquals(0L, product.getQuantity());
    }

    @Test
    void decreaseQuantity_when_exceed_quantity_should_be_fail() {
        // given
        BigDecimal price = BigDecimal.valueOf(10000);
        Long quantity = 10L;
        Product product = new Product(1L, "product", price, quantity, Optional.empty());
        // when
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> product.decreaseQuantity(11L));
    }
}
