package store.core.model.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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

    public boolean existsByNameAndPromotionIsNull(String name) {
        for (Entry<Long, Product> entry : this.storage.entrySet()) {
            Product product = entry.getValue();
            if (product.getName().equals(name) && product.getPromotion().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int countByName(String name) {
        List<Product> result = new ArrayList<>();
        for (Entry<Long, Product> entry : this.storage.entrySet()) {
            Product product = entry.getValue();
            if (product.getName().equals(name)) {
                result.add(product);
            }
        }
        return result.size();
    }

    public void deleteByNameAndPrice(String name, Long quantity) {
        for (Entry<Long, Product> entry : this.storage.entrySet()) {
            Product product = entry.getValue();
            if (product.getName().equals(name) && product.getQuantity().equals(quantity)) {
                deleteById(entry.getKey());
                return;
            }
        }
    }
}
