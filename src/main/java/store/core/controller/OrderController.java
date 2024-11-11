package store.core.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import store.commons.data.infrastructure.DataSourceInitializer;
import store.commons.util.Retry;
import store.commons.util.Runner;
import store.core.dto.OrderSheetDto;
import store.core.dto.OrderSheetDto.OrderSheetItemDto;
import store.core.dto.ProductDto;
import store.core.dto.ReceiptDto;
import store.core.model.Order;
import store.core.model.Product;
import store.core.model.ProductWindow;
import store.core.model.Receipt;
import store.core.model.repository.ProductRepository;
import store.core.model.repository.ProductWindowRepository;
import store.core.view.OrderSheetInputView;
import store.core.view.ProductListOutputView;
import store.core.view.ReceiptOutputView;
import store.core.view.YesOrNoInputView;

public class OrderController {

    private final ProductListOutputView productListOutputView;

    private final OrderSheetInputView orderSheetInputView;

    private final YesOrNoInputView yesOrNoInputView;

    private final ReceiptOutputView receiptOutputView;

    private final ProductRepository productRepository;

    private final ProductWindowRepository productWindowRepository;

    public OrderController(ProductListOutputView productListOutputView,
                           OrderSheetInputView orderSheetInputView,
                           YesOrNoInputView yesOrNoInputView,
                           ReceiptOutputView receiptOutputView,
                           ProductRepository productRepository,
                           ProductWindowRepository productWindowRepository) {
        DataSourceInitializer.getInstance().initialize();

        this.productListOutputView = productListOutputView;
        this.orderSheetInputView = orderSheetInputView;
        this.yesOrNoInputView = yesOrNoInputView;
        this.receiptOutputView = receiptOutputView;
        this.productRepository = productRepository;
        this.productWindowRepository = productWindowRepository;
    }

    public void run() {
        boolean isRestart;
        do {
            Object result;
            result = Runner.run(() -> {
                OrderSheetDto orderSheet = displayProductListAndInputOrderSheet();
                Order order = new Order(DateTimes.now().toLocalDate());
                if (processOrder(orderSheet, order)) return true;
                displayReceipt(order);
                return choiceRestartOrder();
            });
            isRestart = result != null && (boolean) result;
        } while (!isRestart);
    }

    private boolean processOrder(OrderSheetDto orderSheet, Order order) {
        List<ProductWindow> updatedProductWindows = new ArrayList<>();
        addOrderItems(orderSheet, order, updatedProductWindows);
        choiceApplyMembershipDiscount(order);
        this.productWindowRepository.saveAll(updatedProductWindows);
        return false;
    }

    private void addOrderItems(OrderSheetDto orderSheet, Order order, List<ProductWindow> updatedProductWindows) {
        for (OrderSheetItemDto orderSheetItem : orderSheet.items()) {
            String orderProductName = orderSheetItem.name();
            Long orderQuantity = orderSheetItem.quantity();
            ProductWindow productWindow = productWindowRepository.findByName(orderProductName).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요."));
            orderQuantity = getApplicablePromotionQuantityIfApplicable(productWindow, orderQuantity, order, orderProductName);
            orderQuantity = checkInsufficientPromotionQuantityIfExceed(productWindow, orderQuantity, order, orderProductName);
            order.addItem(productWindow, orderQuantity);
            updatedProductWindows.add(productWindow);
        }
    }

    private OrderSheetDto displayProductListAndInputOrderSheet() {
        List<Product> products = this.productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map(ProductDto::modelOf).toList();
        this.productListOutputView.display(productDtos);

        String content = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
        return orderSheetInputView.displayWithInput(content);
    }

    private Long getApplicablePromotionQuantityIfApplicable(ProductWindow productWindow, Long orderQuantity, Order order, String orderProductName) {
        AtomicLong atomicOrderQuantity = new AtomicLong(orderQuantity);
        Long applicablePromotionQuantity = productWindow.getRequiredPromotionQuantityIfApplicable(orderQuantity, order.getOrderDate());
        if (applicablePromotionQuantity > 0) {
            orderQuantity = Retry.retry(5, () -> {
                String content = "현재 " + orderProductName + "은(는) " + applicablePromotionQuantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
                boolean isApplicablePromotion = yesOrNoInputView.displayWithInput(content);
                if (isApplicablePromotion) {
                    atomicOrderQuantity.addAndGet(applicablePromotionQuantity);
                }
                return atomicOrderQuantity.get();
            });
        }
        return orderQuantity;
    }

    private Long checkInsufficientPromotionQuantityIfExceed(ProductWindow productWindow, Long orderQuantity, Order order, String orderProductName) {
        Long changedQuantity = orderQuantity;
        Long insufficientQuantity = productWindow.getInsufficientPromotionQuantityIfExceed(orderQuantity, order.getOrderDate());
        if (insufficientQuantity > 0) {
            boolean result = Retry.retry(5, () -> {
                String content = "현재 " + orderProductName + " " + insufficientQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
                return yesOrNoInputView.displayWithInput(content);
            });
            if (!result) { changedQuantity -= insufficientQuantity; }
        }
        return changedQuantity;
    }

    private void choiceApplyMembershipDiscount(Order order) {
        Retry.retry(5, () -> {
            String content = "멤버십 할인을 받으시겠습니까? (Y/N)";
            boolean isApplyMembershipDiscount = yesOrNoInputView.displayWithInput(content);
            if (isApplyMembershipDiscount) {
                order.applyMembership();
            }
            return order;
        });
    }

    private void displayReceipt(Order order) {
        Receipt receipt = order.getReceipt();
        ReceiptDto receiptDto = ReceiptDto.modelOf(receipt);
        receiptOutputView.display(receiptDto);
    }

    private Boolean choiceRestartOrder() {
        String content = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
        boolean isRestartOrder = yesOrNoInputView.displayWithInput(content);
        return !isRestartOrder;
    }
}
