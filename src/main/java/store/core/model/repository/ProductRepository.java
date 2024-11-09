package store.core.model.repository;

import store.commons.data.repository.SimpleRepository;
import store.core.model.Product;

public class ProductRepository extends SimpleRepository<Product, Long> {

    private static ProductRepository instance;

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    private ProductRepository() {
        super();
    }
}
