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
        int buy = 2;
        int get = 1;
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
        Promotion promotion1 = new Promotion("equals", 1, 1, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Promotion promotion2 = new Promotion("equals", 2, 1, LocalDate.of(2024, 11, 10), LocalDate.of(2024, 11, 20));
        Promotion promotion3 = new Promotion("not_equals", 1, 1, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        // then
        Assertions.assertEquals(promotion1, promotion2);
        Assertions.assertNotEquals(promotion1, promotion3);
        Assertions.assertNotEquals(promotion2, promotion3);
    }
}
