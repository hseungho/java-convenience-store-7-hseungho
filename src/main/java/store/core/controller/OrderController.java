package store.core.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.Optional;
import store.core.dto.OrderSheetDto;
import store.core.dto.OrderSheetDto.OrderSheetItemDto;
import store.core.dto.ProductDto;
import store.core.model.Product;
import store.core.model.ProductWindow;
import store.core.model.Promotion;
import store.core.model.repository.ProductRepository;
import store.core.model.repository.ProductWindowRepository;
import store.core.view.OrderSheetInputView;
import store.core.view.ProductListOutputView;

public class OrderController {

    private final ProductListOutputView productListOutputView;

    private final OrderSheetInputView orderSheetInputView;

    private final ProductRepository productRepository;

    private final ProductWindowRepository productWindowRepository;

    public OrderController(ProductListOutputView productListOutputView,
                           OrderSheetInputView orderSheetInputView,
                           ProductRepository productRepository,
                           ProductWindowRepository productWindowRepository) {
        this.productListOutputView = productListOutputView;
        this.orderSheetInputView = orderSheetInputView;
        this.productRepository = productRepository;
        this.productWindowRepository = productWindowRepository;
    }

    public void run() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map(ProductDto::modelOf).toList();
        productListOutputView.display(productDtos);

        OrderSheetDto orderSheet = orderSheetInputView.displayWithInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        System.out.println(orderSheet);

        for (OrderSheetItemDto orderSheetItem : orderSheet.items()) {
            ProductWindow productWindow = productWindowRepository.findByName(orderSheetItem.name())
                    .orElseThrow(IllegalArgumentException::new);
            Optional<Product> promotionProduct = productWindow.findPromotionProduct();
            if (promotionProduct.isEmpty() || promotionProduct.get().getPromotion().isEmpty()) {
                continue;
            }
            Promotion promotion = promotionProduct.get().getPromotion().get();
            int promotionApplyQuantity = promotion.canApply(orderSheetItem.quantity(), DateTimes.now().toLocalDate());
            if (promotionApplyQuantity < 0) {
                continue;
            }
            // 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
            System.out.println("현재 " + orderSheetItem.name() + "은(는) " + promotionApplyQuantity + "개를 무료로 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        }
    }
}
