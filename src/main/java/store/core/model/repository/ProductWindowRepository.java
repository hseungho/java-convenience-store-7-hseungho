package store.core.model.repository;

import store.commons.data.repository.SimpleRepository;
import store.core.model.ProductWindow;

public class ProductWindowRepository extends SimpleRepository<ProductWindow, String> {

    private static ProductWindowRepository instance;

    public static ProductWindowRepository getInstance() {
        if (instance == null) {
            instance = new ProductWindowRepository();
        }
        return instance;
    }

    private ProductWindowRepository() {
        super();
    }
}
