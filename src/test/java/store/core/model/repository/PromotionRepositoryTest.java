package store.core.model.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.core.model.Promotion;

class PromotionRepositoryTest {

    private PromotionRepository promotionRepository;

    @BeforeEach
    void initialize() {
        promotionRepository = PromotionRepository.getInstance();
    }

    @AfterEach
    void rollback() {
        PromotionRepository.getInstance().deleteAll();
    }

    @Test
    void save_should_be_pass() {
        // given
        Promotion promotion = new Promotion("new promotion", 1L, 1L, LocalDate.now(), LocalDate.now());
        // when
        Promotion persistedPromotion = promotionRepository.save(promotion);
        // then
        Assertions.assertNotNull(persistedPromotion);
        Assertions.assertEquals(promotion, persistedPromotion);
    }

    @Test
    void save_given_exists_same_promotions_should_be_pass_and_update() {
        // given
        Promotion promotion = new Promotion("new promotion", 1L, 1L, LocalDate.now(), LocalDate.now());
        promotionRepository.save(promotion);
        // when
        Promotion updatePromotion = new Promotion("new promotion", 2L, 2L, LocalDate.now(), LocalDate.now());
        Promotion persistedPromotion = promotionRepository.save(updatePromotion);
        // then
        Assertions.assertEquals(promotion, persistedPromotion);
        Assertions.assertEquals(updatePromotion, persistedPromotion);
        Assertions.assertEquals(2, persistedPromotion.getBuy());
        Assertions.assertEquals(2, persistedPromotion.getGet());
    }

    @Test
    void saveAll_and_findAll_should_be_pass() {
        // given
        List<Promotion> promotions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            promotions.add(new Promotion("promotion_" + i, 2L, 1L, LocalDate.now(), LocalDate.now()));
        }
        // when
        promotionRepository.saveAll(promotions);
        // then
        List<Promotion> persistedPromotions = promotionRepository.findAll();
        Assertions.assertEquals(10, persistedPromotions.size());
        Assertions.assertEquals(promotions, persistedPromotions);
    }

    @Test
    void findById_when_exists_should_be_pass() {
        // given
        Promotion promotion = new Promotion("new promotion", 1L, 1L, LocalDate.now(), LocalDate.now());
        promotionRepository.save(promotion);
        // when
        Optional<Promotion> foundPromotion = promotionRepository.findByName("new promotion");
        // then
        Assertions.assertTrue(foundPromotion.isPresent());
        Assertions.assertEquals(promotion, foundPromotion.get());
    }

    @Test
    void findById_when_not_exists_should_be_pass() {
        // given
        Promotion promotion = new Promotion("new promotion", 1L, 1L, LocalDate.now(), LocalDate.now());
        promotionRepository.save(promotion);
        // when
        Optional<Promotion> notFoundPromotion = promotionRepository.findByName("not found promotion");
        // then
        Assertions.assertFalse(notFoundPromotion.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, notFoundPromotion::get);
    }
}
