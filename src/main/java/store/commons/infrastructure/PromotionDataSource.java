package store.commons.infrastructure;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import store.commons.lang.DataLoaderException;
import store.core.model.Promotion;
import store.core.model.repository.PromotionRepository;

public class PromotionDataSource extends AbstractDataSource<Promotion> {

    private static final String PATH_PROMOTIONS = "src/main/resources/promotions.md";

    private final PromotionRepository promotionRepository;

    public PromotionDataSource(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void initialize() {
        List<Promotion> data = this.read();
        this.promotionRepository.saveAll(data);
    }

    @Override
    protected String getPath() {
        return PATH_PROMOTIONS;
    }

    @Override
    protected boolean isSkipFirstLine() {
        return true;
    }

    @Override
    protected Promotion map(String line) {
        String[] array = this.split(line);

        String name = this.getStringOrThrow("name", array[0]);
        int buy = this.getIntegerOrThrow("buy", array[1]);
        int get = this.getIntegerOrThrow("get", array[2]);
        LocalDate startDate = this.getLocalDateOrThrow("startDate", array[3]);
        LocalDate endDate = this.getLocalDateOrThrow("endDate", array[4]);

        return new Promotion(name, buy, get, startDate, endDate);
    }

    private String[] split(String line) {
        String[] array = line.split(",");
        validateSplitLength(array);
        return array;
    }

    private void validateSplitLength(String[] array) {
        if (array.length != 5) {
            throw new DataLoaderException(this.getPath() + " 파일에 올바르지 않은 형식으로 데이터가 저장되어 있습니다.");
        }
    }

    private String getStringOrThrow(String property, String value) {
        if (value == null) {
            throw new DataLoaderException(property + "에 해당하는 값이 존재하지 않습니다.");
        }
        return value;
    }

    private Integer getIntegerOrThrow(String property, String value) {
        if (value == null) {
            throw new DataLoaderException(property + "에 해당하는 값이 존재하지 않습니다.");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new DataLoaderException(property + "에 해당하는 값이 숫자가 아닙니다.");
        }
    }

    private LocalDate getLocalDateOrThrow(String property, String value) {
        if (value == null) {
            throw new DataLoaderException(property + "에 해당하는 값이 존재하지 않습니다.");
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new DataLoaderException(property + "에 해당하는 값이 날짜 형식이 아닙니다.");
        }
    }
}
