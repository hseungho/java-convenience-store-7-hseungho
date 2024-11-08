package store.commons.infrastructure;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import store.core.model.Promotion;
import store.core.model.repository.PromotionRepository;

class PromotionDataSourceTest {

    @Test
    void initialize_should_be_pass() {
        // given
        PromotionRepository promotionRepository = PromotionRepository.getInstance();
        AbstractDataSource<Promotion> dataSource = new PromotionDataSource(promotionRepository);
        // when
        dataSource.initialize();
        // then
        List<Promotion> promotions = promotionRepository.findAll();
        Assertions.assertEquals(3, promotions.size());
        Assertions.assertEquals("탄산2+1", promotions.get(0).getName());
        Assertions.assertEquals("MD추천상품", promotions.get(1).getName());
        Assertions.assertEquals("반짝할인", promotions.get(2).getName());
    }
}
