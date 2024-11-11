package store;

import store.commons.data.infrastructure.DataSourceInitializer;
import store.core.controller.OrderController;
import store.core.model.repository.ProductRepository;
import store.core.model.repository.ProductWindowRepository;
import store.core.view.OrderSheetInputView;
import store.core.view.ProductListOutputView;
import store.core.view.ReceiptOutputView;
import store.core.view.YesOrNoInputView;

public class Application {
    public static void main(String[] args) {
        DataSourceInitializer.getInstance().initialize();

        ProductListOutputView productListOutputView = new ProductListOutputView();
        OrderSheetInputView orderSheetInputView = new OrderSheetInputView();
        YesOrNoInputView yesOrNoInputView = new YesOrNoInputView();
        ReceiptOutputView receiptOutputView = new ReceiptOutputView();
        ProductRepository productRepository = ProductRepository.getInstance();
        ProductWindowRepository productWindowRepository = ProductWindowRepository.getInstance();

        OrderController controller = new OrderController(
                productListOutputView,
                orderSheetInputView,
                yesOrNoInputView,
                receiptOutputView,
                productRepository,
                productWindowRepository
        );
        controller.run();
    }
}
