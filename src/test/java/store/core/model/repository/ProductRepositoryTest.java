package store.core.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.core.model.Product;
import store.core.model.Promotion;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void initialize() {
        productRepository = ProductRepository.getInstance();
    }

    @AfterEach
    void rollback() {
        ProductRepository.getInstance().deleteAll();
    }

    @Test
    void save_should_be_pass() {
        // given
        Product product = new Product("new product", BigDecimal.valueOf(1000), 10L, null);
        // when
        Product persistedProduct = productRepository.save(product);
        // then
        Assertions.assertNotNull(persistedProduct);
        Assertions.assertEquals(product, persistedProduct);
    }

    @Test
    void save_given_exists_should_be_pass_and_update() {
        // given
        Product product = new Product(1L, "new product", BigDecimal.valueOf(1000), 10L, null);
        productRepository.save(product);
        // when
        Promotion newPromotion = new Promotion("promotion", 2L, 1L, LocalDate.now(), LocalDate.now());
        Product updatedProduct = new Product(1L, "new product", BigDecimal.valueOf(20000), 20L, null);
        Product persistedProduct = productRepository.save(updatedProduct);
        // then
        Assertions.assertEquals(product, persistedProduct);
        Assertions.assertEquals(updatedProduct, persistedProduct);
        Assertions.assertEquals(BigDecimal.valueOf(20000), persistedProduct.getPrice());
        Assertions.assertEquals(20, persistedProduct.getQuantity());
        Assertions.assertEquals(newPromotion, persistedProduct.getPromotion());
    }

    @Test
    void saveAll_and_findAll_should_be_pass() {
        // given
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            products.add(new Product("product_" + i, BigDecimal.valueOf(1000), 10L, null));
        }
        // when
        productRepository.saveAll(products);
        // then
        List<Product> persistedProducts = productRepository.findAll();
        Assertions.assertEquals(10, persistedProducts.size());
        Assertions.assertEquals(products, persistedProducts);
    }

    @Test
    void findById_when_exists_should_be_pass() {
        // given
        Product product = new Product(1L, "product", BigDecimal.valueOf(1000), 10L, null);
        productRepository.save(product);
        // when
        Optional<Product> foundProduct = productRepository.findById(1L);
        // then
        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product, foundProduct.get());
    }

    @Test
    void findById_when_not_exists_should_be_pass() {
        // given
        Product product = new Product(1L, "product", BigDecimal.valueOf(1000), 10L, null);
        productRepository.save(product);
        // when
        Optional<Product> notFoundProduct = productRepository.findById(2L);
        // then
        Assertions.assertFalse(notFoundProduct.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, notFoundProduct::get);
    }
}
