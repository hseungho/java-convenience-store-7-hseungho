package store.commons.data.infrastructure;

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
    }
}
