package store.commons.data.infrastructure;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.core.model.Product;
import store.core.model.ProductWindow;
import store.core.model.repository.ProductRepository;
import store.core.model.repository.ProductWindowRepository;
import store.core.model.repository.PromotionRepository;

class ProductDataSourceTest {

    @BeforeEach
    void clearPersistence() {
        ProductRepository.getInstance().deleteAll();
        PromotionRepository.getInstance().deleteAll();
        ProductWindowRepository.getInstance().deleteAll();
    }

    @Test
    void initialize_should_be_pass() {
        // given
        ProductRepository productRepository = ProductRepository.getInstance();
        PromotionRepository promotionRepository = PromotionRepository.getInstance();
        ProductWindowRepository productWindowRepository = ProductWindowRepository.getInstance();
        AbstractDataSource<Product> dataSource = new ProductDataSource(
                productRepository, promotionRepository, productWindowRepository
        );
        // when
        dataSource.initialize();
        // then
        List<Product> products = productRepository.findAll();
        Assertions.assertEquals(16, products.size());
        List<ProductWindow> windows = productWindowRepository.findAll();
        Assertions.assertEquals(11, windows.size());
        Optional<ProductWindow> window = productWindowRepository.findById("콜라");
        Assertions.assertTrue(window.isPresent());
    }
}
