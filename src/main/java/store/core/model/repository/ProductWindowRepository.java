package store.core.model.repository;

import java.util.Optional;
import store.commons.data.repository.SimpleRepository;
import store.core.model.Product;
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

    @Override
    public ProductWindow save(ProductWindow entity) {
        Product product = entity.getProduct();
        Product promotionProduct = entity.getPromotionProduct();
        if (product == null || promotionProduct == null) {
            return super.save(entity);
        }
        migratePromotionQuantityToProductQuantityIfRequired(product, promotionProduct, entity);
        entity.setIsNew(false);
        return super.save(entity);
    }

    private void migratePromotionQuantityToProductQuantityIfRequired(Product product, Product promotionProduct, ProductWindow entity) {
        ProductRepository productRepository = ProductRepository.getInstance();
        if (product.getId() != null && promotionProduct.getId() != null && !entity.isNew() &&
                promotionProduct.getQuantity() < promotionProduct.getPromotion().getPromotionSets()) {
            Long promotionQuantity = promotionProduct.getQuantity();
            product.increaseQuantity(promotionQuantity);
            promotionProduct.decreaseQuantity(promotionQuantity);
            productRepository.save(product);
            productRepository.save(promotionProduct);
        }
    }

    public Optional<ProductWindow> findByName(String id) {
        return findById(id);
    }
}
