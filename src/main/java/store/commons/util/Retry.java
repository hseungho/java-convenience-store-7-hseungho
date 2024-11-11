package store.commons.util;

import java.util.function.Supplier;
import store.commons.lang.InputOverFlowException;

public class Retry {

    private Retry() {}

    public static <T> T retry(int repeat, Supplier<T> supplier) {
        T result;
        int count = 0;
        do {
            if (++count > repeat) {
                throw new InputOverFlowException();
            }
            result = Runner.run(supplier);
        } while (result == null);
        return result;
    }
}
