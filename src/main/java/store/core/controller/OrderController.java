package store.core.controller;

import java.util.List;
import store.core.dto.OrderSheetDto;
import store.core.dto.ProductDto;
import store.core.model.Product;
import store.core.model.repository.ProductRepository;
import store.core.view.OrderSheetInputView;
import store.core.view.ProductListOutputView;

public class OrderController {

    private final ProductListOutputView productListOutputView;

    private final OrderSheetInputView orderSheetInputView;

    private final ProductRepository productRepository;

    public OrderController(ProductListOutputView productListOutputView,
                           OrderSheetInputView orderSheetInputView,
                           ProductRepository productRepository) {
        this.productListOutputView = productListOutputView;
        this.orderSheetInputView = orderSheetInputView;
        this.productRepository = productRepository;
    }

    public void run() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map(ProductDto::modelOf).toList();
        productListOutputView.display(productDtos);

        OrderSheetDto orderSheet = orderSheetInputView.displayWithInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        System.out.println(orderSheet);
    }
}
