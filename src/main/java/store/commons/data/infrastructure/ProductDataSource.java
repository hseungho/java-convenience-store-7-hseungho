package store.commons.data.infrastructure;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import store.core.model.Product;
import store.core.model.ProductWindow;
import store.core.model.Promotion;
import store.core.model.repository.ProductRepository;
import store.core.model.repository.ProductWindowRepository;
import store.core.model.repository.PromotionRepository;

public class ProductDataSource extends AbstractDataSource<Product> {

    private static final String PATH = "src/main/resources/products.md";

    private final ProductRepository productRepository;

    private final PromotionRepository promotionRepository;

    private final ProductWindowRepository productWindowRepository;

    public ProductDataSource(ProductRepository productRepository,
                             PromotionRepository promotionRepository,
                             ProductWindowRepository productWindowRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.productWindowRepository = productWindowRepository;
    }

    @Override
    public void initialize() {
        List<Product> data = this.read();
        saveProducts(data);
        List<ProductWindow> windows = groupByProductName(this.productRepository.findAll());
        this.productWindowRepository.saveAll(windows);
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected boolean isIgnoreColumnHeader() {
        return true;
    }

    @Override
    protected boolean isPresentIdColumn() {
        return true;
    }

    @Override
    protected Product map(String line) {
        String[] columns = parseToColumns(Product.class, line);

        String name = this.getStringOrThrow("name", columns[0]);
        BigDecimal price = this.getBigDecimalOrThrow("price", columns[1]);
        Long quantity = this.getLongOrThrow("quantity", columns[2]);
        Optional<Promotion> promotion = promotionRepository.findByName(columns[3]);

        return new Product(name, price, quantity, promotion.orElse(null));
    }

    private BigDecimal getBigDecimalOrThrow(String property, String value) {
        if (value == null) {
            throw new DataLoaderException(property + "에 해당하는 값이 존재하지 않습니다.");
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new DataLoaderException(property + "에 해당하는 값이 숫자가 아닙니다.");
        }
    }

    private void saveProducts(List<Product> products) {
        for (Product product : products) {
            this.productRepository.save(product);
            if (!this.productRepository.existsByNameAndPromotionIsNull(product.getName())) {
                this.productRepository.save(new Product(product.getName(), product.getPrice()));
            }
            if (this.productRepository.countByName(product.getName()) > 2) {
                this.productRepository.deleteByNameAndPrice(product.getName(), 0L);
            }
        }
    }

    private List<ProductWindow> groupByProductName(List<Product> allProducts) {
        Map<String, List<Product>> map = allProducts.stream()
                .collect(Collectors.groupingBy(Product::getName));

        return map.entrySet().stream().map(entry -> {
            String name = entry.getKey();
            List<Product> products = entry.getValue();
            Product product = findHasNotPromotion(products);
            Product promotionProduct = findHasPromotion(products);
            return new ProductWindow(name, product, promotionProduct);
        }).toList();
    }

    private Product findHasNotPromotion(List<Product> products) {
        List<Product> hasNotPromotions = products.stream().filter(Product::hasNotPromotion).toList();
        if (hasNotPromotions.isEmpty()) {
            return null;
        }
        return hasNotPromotions.getFirst();
    }

    private Product findHasPromotion(List<Product> products) {
        List<Product> hasPromotions = products.stream().filter(Product::hasPromotion).toList();
        if (hasPromotions.isEmpty()) {
            return null;
        }
        return hasPromotions.getFirst();
    }
}
