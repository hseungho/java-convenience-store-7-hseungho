package store.core.model;

import java.time.LocalDate;
import java.util.Objects;
import store.commons.data.repository.Id;

public class Promotion {

    @Id
    private final String name;

    private final int buy;

    private final int get;

    private final LocalDate startDate;

    private final LocalDate endDate;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return this.name;
    }

    public int getBuy() {
        return this.buy;
    }

    public int getGet() {
        return this.get;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public int canApply(Long quantity, LocalDate date) {
        if (!isActiveAt(date) || quantity >= this.buy) {
            return -1;
        }
        return (int) (this.buy - quantity);
    }

    public Long getPromotionQuantity(Long quantity, LocalDate date) {
        if (!isActiveAt(date)) {
            return -1L;
        }
        return quantity / this.buy * this.get;
    }

    private boolean isActiveAt(LocalDate date) {
        return (this.startDate.isBefore(date) || this.startDate.isEqual(date)) &&
                (this.endDate.isAfter(date) || this.endDate.isEqual(date));
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
