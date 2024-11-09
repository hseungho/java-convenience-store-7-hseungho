package store.core.model.repository;

import java.util.Optional;
import store.commons.data.repository.SimpleRepository;
import store.core.model.Promotion;

public class PromotionRepository extends SimpleRepository<Promotion, String> {

    private static PromotionRepository instance;

    public static PromotionRepository getInstance() {
        if (instance == null) {
            instance = new PromotionRepository();
        }
        return instance;
    }

    private PromotionRepository() {
        super();
    }

    public Optional<Promotion> findByName(String name) {
        return findById(name);
    }
}
