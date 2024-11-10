package store.commons.data.infrastructure;

import store.core.model.repository.ProductRepository;
import store.core.model.repository.ProductWindowRepository;
import store.core.model.repository.PromotionRepository;

public class DataSourceInitializer {

    private static DataSourceInitializer instance;

    public static DataSourceInitializer getInstance() {
        if (instance == null) {
            instance = new DataSourceInitializer();
        }
        return instance;
    }

    private DataSourceInitializer() {}

    public void initialize() {
        PromotionRepository promotionRepository = PromotionRepository.getInstance();
        ProductRepository productRepository = ProductRepository.getInstance();
        ProductWindowRepository productWindowRepository = ProductWindowRepository.getInstance();

        PromotionDataSource promotionDataSource = new PromotionDataSource(promotionRepository);
        promotionDataSource.initialize();
        ProductDataSource productDataSource = new ProductDataSource(
                productRepository,
                promotionRepository,
                productWindowRepository
        );
        productDataSource.initialize();
    }
}
