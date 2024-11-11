package store.core.constants;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Numbers {

    private Numbers() {}

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

    public static String formatDecimal(BigDecimal number) {
        return DECIMAL_FORMAT.format(number);
    }
}
