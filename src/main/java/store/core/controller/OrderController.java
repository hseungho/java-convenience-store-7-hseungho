package store.core.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.core.dto.OrderSheetDto;
import store.core.dto.OrderSheetDto.OrderSheetItemDto;
import store.core.dto.ProductDto;
import store.core.model.Order;
import store.core.model.Product;
import store.core.model.ProductWindow;
import store.core.model.repository.ProductRepository;
import store.core.model.repository.ProductWindowRepository;
import store.core.view.OrderSheetInputView;
import store.core.view.ProductListOutputView;
import store.core.view.YesOrNoInputView;

public class OrderController {

    private final ProductListOutputView productListOutputView;

    private final OrderSheetInputView orderSheetInputView;

    private final YesOrNoInputView yesOrNoInputView;

    private final ProductRepository productRepository;

    private final ProductWindowRepository productWindowRepository;

    public OrderController(ProductListOutputView productListOutputView,
                           OrderSheetInputView orderSheetInputView,
                           YesOrNoInputView yesOrNoInputView,
                           ProductRepository productRepository,
                           ProductWindowRepository productWindowRepository) {
        this.productListOutputView = productListOutputView;
        this.orderSheetInputView = orderSheetInputView;
        this.yesOrNoInputView = yesOrNoInputView;
        this.productRepository = productRepository;
        this.productWindowRepository = productWindowRepository;
    }

    public void run() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map(ProductDto::modelOf).toList();
        productListOutputView.display(productDtos);

        OrderSheetDto orderSheet = orderSheetInputView.displayWithInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");

        LocalDate date = DateTimes.now().toLocalDate();
        Order order = new Order(date);

        List<ProductWindow> updatedProductWindows = new ArrayList<>();
        for (OrderSheetItemDto orderSheetItem : orderSheet.items()) {
            String orderProductName = orderSheetItem.name();
            Long orderQuantity = orderSheetItem.quantity();
            ProductWindow productWindow = productWindowRepository.findByName(orderProductName)
                    .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 상품이 존재하지 않습니다: " + orderProductName));

            Long applicablePromotionQuantity = productWindow.getRequiredPromotionQuantityIfApplicable(orderQuantity, order.getOrderDate());
            if (applicablePromotionQuantity > 0) {
                boolean isApplicablePromotion = yesOrNoInputView.displayWithInput("현재 " + orderProductName + "은(는) " + applicablePromotionQuantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
                if (isApplicablePromotion) {
                    orderQuantity += applicablePromotionQuantity;
                }
            }

            Long insufficientQuantity = productWindow.getInsufficientPromotionQuantityIfExceed(orderQuantity, order.getOrderDate());
            if (insufficientQuantity > 0) {
                boolean applyPromotion = yesOrNoInputView.displayWithInput("현재 " + orderProductName + " " + insufficientQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
                if (!applyPromotion) {
                    return;
                }
            }

            order.addItem(productWindow, orderQuantity);


            updatedProductWindows.add(productWindow);
        }

        boolean isApplyMembershipDiscount = yesOrNoInputView.displayWithInput("멤버십 할인을 받으시겠습니까? (Y/N)");
        if (isApplyMembershipDiscount) {
            order.applyMembership();
        }

        this.productWindowRepository.saveAll(updatedProductWindows);

        System.out.println();
        System.out.println(order);
        System.out.println(order.getPayment());

        List<ProductDto> afters = this.productRepository.findAll().stream().map(ProductDto::modelOf).toList();
        productListOutputView.display(afters);
    }
}
