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
        this.productRepository.saveAll(data);
        groupByProductName(this.productRepository.findAll());
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

        return new Product(name, price, quantity, promotion);
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

    private void groupByProductName(List<Product> allProducts) {
        Map<String, List<Product>> map = allProducts.stream()
                .collect(Collectors.groupingBy(Product::getName));
        List<ProductWindow> windows = map.entrySet().stream().map(entry -> {
            String name = entry.getKey();
            List<Product> products = entry.getValue();
            return new ProductWindow(name, products);
        }).toList();
        this.productWindowRepository.saveAll(windows);
    }
}
