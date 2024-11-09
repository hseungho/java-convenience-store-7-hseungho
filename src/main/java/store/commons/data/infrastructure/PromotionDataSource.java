package store.commons.data.infrastructure;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
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
    protected boolean isIgnoreColumnHeader() {
        return true;
    }

    @Override
    protected boolean isPresentIdColumn() {
        return false;
    }

    @Override
    protected Promotion map(String line) {
        String[] columns = this.parseToColumns(Promotion.class, line);

        String name = this.getStringOrThrow("name", columns[0]);
        int buy = this.getIntegerOrThrow("buy", columns[1]);
        int get = this.getIntegerOrThrow("get", columns[2]);
        LocalDate startDate = this.getLocalDateOrThrow("startDate", columns[3]);
        LocalDate endDate = this.getLocalDateOrThrow("endDate", columns[4]);

        return new Promotion(name, buy, get, startDate, endDate);
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
