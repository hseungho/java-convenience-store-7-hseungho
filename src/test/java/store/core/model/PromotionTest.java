package store.core.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PromotionTest {

    @Test
    void create_should_be_pass() {
        // given
        // when
        String name = "promotion name";
        Long buy = 2L;
        Long get = 1L;
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 30);
        Promotion promotion = new Promotion(name, buy, get, startDate, endDate);
        // then
        Assertions.assertEquals(name, promotion.getName());
        Assertions.assertEquals(buy, promotion.getBuy());
        Assertions.assertEquals(get, promotion.getGet());
        Assertions.assertEquals(startDate, promotion.getStartDate());
        Assertions.assertEquals(endDate, promotion.getEndDate());
    }

    @Test
    void equals_should_be_pass() {
        // given
        // when
        Promotion promotion1 = new Promotion("equals", 1L, 1L, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Promotion promotion2 = new Promotion("equals", 2L, 1L, LocalDate.of(2024, 11, 10), LocalDate.of(2024, 11, 20));
        Promotion promotion3 = new Promotion("not_equals", 1L, 1L, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        // then
        Assertions.assertEquals(promotion1, promotion2);
        Assertions.assertNotEquals(promotion1, promotion3);
        Assertions.assertNotEquals(promotion2, promotion3);
    }

    @Test
    void getPromotionQuantity_should_be_pass() {
        // given
        Promotion promotion = new Promotion("promotion", 2L, 1L, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        // when
        Long quantity = 10L;
        LocalDate date = LocalDate.of(2024, 11, 15);
        Long promotionQuantity = promotion.getPromotionQuantity(quantity, date);
        // then
        Assertions.assertEquals(5L, promotionQuantity);
    }
    
    @Test
    void getPromotionQuantity_when_not_between_start_and_end_should_return_negative_one() {
        // given
        Promotion promotion = new Promotion("promotion", 2L, 1L, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        // when
        Long quantity = 10L;
        LocalDate date = LocalDate.of(2024, 12, 1);
        Long promotionQuantity = promotion.getPromotionQuantity(quantity, date);
        // then
        Assertions.assertTrue(promotionQuantity < 0);
        Assertions.assertEquals(-1, promotionQuantity);
    }
}
