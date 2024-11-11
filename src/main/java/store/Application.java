package store;

import store.commons.beans.BeanFactory;
import store.commons.lang.InputOverFlowException;
import store.commons.lang.ProgramExitException;
import store.commons.logger.Logger;
import store.core.controller.OrderController;

public class Application {
    public static void main(String[] args) {
        Application.run(() -> {
            OrderController orderController = BeanFactory.getBean(OrderController.class);
            orderController.run();
        });
    }

    private static void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (ProgramExitException e) {
            Logger.info(e.getMessage());
        } catch (InputOverFlowException e) {
            Logger.error(e.getMessage());
        } catch (Throwable e) {
            Logger.error(e);
        }
    }
}
