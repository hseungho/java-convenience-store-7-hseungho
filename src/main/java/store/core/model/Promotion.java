package store.core.model;

import java.time.LocalDate;
import java.util.Objects;
import store.commons.data.repository.Id;

public class Promotion {

    @Id
    private final String name;

    private final Long buy;

    private final Long get;

    private final LocalDate startDate;

    private final LocalDate endDate;

    public Promotion(String name, Long buy, Long get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return this.name;
    }

    public Long getBuy() {
        return this.buy;
    }

    public Long getGet() {
        return this.get;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Long getPromotionSets() {
        return this.buy + this.get;
    }

    public Long getPromotionQuantity(Long quantity, LocalDate date) {
        if (isInactiveAt(date)) {
            return 0L;
        }
        return quantity / this.buy * this.get;
    }

    public boolean isActiveAt(LocalDate date) {
        return (this.startDate.isBefore(date) || this.startDate.isEqual(date)) &&
                (this.endDate.isAfter(date) || this.endDate.isEqual(date));
    }

    public boolean isInactiveAt(LocalDate date) {
        return !isActiveAt(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promotion promotion)) return false;
        return Objects.equals(name, promotion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
