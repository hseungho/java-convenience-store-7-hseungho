package store.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductWindowTest {

    @Test
    void getRequiredPromotionQualityIfApplicable_should_be_pass() {
        // given
        String name = "product";
        Product product = new Product(name, BigDecimal.valueOf(1000), 10L, null);
        Product promotionProduct = new Product(name, BigDecimal.valueOf(1000), 10L, new Promotion("name", 2L, 1L,
                LocalDate.now(), LocalDate.now()));
        ProductWindow productWindow = new ProductWindow(name, product, promotionProduct);
        // when
        Long required = productWindow.getRequiredPromotionQuantityIfApplicable(2L, LocalDate.now());
        // then
        Assertions.assertEquals(1L, required);
    }

    @Test
    void getInsufficientPromotionQuantityIfExceed_should_be_pass() {
        // given
        String name = "product";
        Product product = new Product(name, BigDecimal.valueOf(1000), 10L, null);
        Product promotionProduct = new Product(name, BigDecimal.valueOf(1000), 7L, new Promotion("name", 2L, 1L,
                LocalDate.now(), LocalDate.now()));
        ProductWindow productWindow = new ProductWindow(name, product, promotionProduct);
        // when
        Long exceed = productWindow.getInsufficientPromotionQuantityIfExceed(10L, LocalDate.now());
        // then
        Assertions.assertEquals(4L, exceed);
    }


    @Test
    void getAvailablePromotionQuantity_should_be_pass() {
        // given
        String name = "product";
        Product product = new Product(name, BigDecimal.valueOf(1000), 10L, null);
        Product promotionProduct = new Product(name, BigDecimal.valueOf(1000), 7L, new Promotion("name", 2L, 1L,
                LocalDate.now(), LocalDate.now()));
        ProductWindow productWindow = new ProductWindow(name, product, promotionProduct);
        // when
        Long availablePromotionQuantity = productWindow.getAvailablePromotionQuantity(6L, LocalDate.now());
        // then
        Assertions.assertEquals(2L, availablePromotionQuantity);
    }

    @Test
    void getAvailableQuantity_should_be_pass() {
        // given
        String name = "product";
        Product product = new Product(name, BigDecimal.valueOf(1000), 10L, null);
        Product promotionProduct = new Product(name, BigDecimal.valueOf(1000), 7L, new Promotion("name", 2L, 1L,
                LocalDate.now(), LocalDate.now()));
        ProductWindow productWindow = new ProductWindow(name, product, promotionProduct);
        // when
        Long availableQuantity = productWindow.getAvailableQuantity();
        // then
        Assertions.assertEquals(10L, availableQuantity);
    }
}
