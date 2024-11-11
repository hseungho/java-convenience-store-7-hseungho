package store.commons.util;

import java.util.function.Supplier;
import store.commons.lang.ProgramExitException;
import store.commons.logger.Logger;

public class Runner {

    private Runner() {}

    public static <T> T runCatching(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (ProgramExitException e) {
            throw e;
        } catch (Throwable t) {
            Logger.error(t.getMessage());
            return null;
        }
    }
}
