package store.commons.util;

import java.util.function.Supplier;

public class Runner {

    private Runner() {}

    public static <T> T run(Supplier<T> supplier) {
        return supplier.get();
    }
}
