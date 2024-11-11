package store.core.constants;

public class Error {

    private Error() {}

    public static final class InputError {

        public static final String INVALID_FORMAT = "올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

        public static final String INVALID_INPUT = "잘못된 입력입니다. 다시 입력해 주세요.";
    }

    public static final class ProductError {

        public static final String EXCEED_QUANTITY = "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    }
}
