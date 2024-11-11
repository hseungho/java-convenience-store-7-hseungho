package store;

import store.commons.beans.BeanFactory;
import store.commons.data.infrastructure.DataSourceInitializer;
import store.core.controller.OrderController;

public class Application {
    public static void main(String[] args) {
        DataSourceInitializer.getInstance().initialize();

        OrderController orderController = BeanFactory.getBean(OrderController.class);
        orderController.run();
    }
}
