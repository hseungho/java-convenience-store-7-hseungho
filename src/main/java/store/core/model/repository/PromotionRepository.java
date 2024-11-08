package store.core.model.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.core.model.Promotion;

public class PromotionRepository {

    private static PromotionRepository instance;

    public static PromotionRepository getInstance() {
        if (instance == null) {
            instance = new PromotionRepository();
        }
        return instance;
    }

    private final ConcurrentHashMap<String, Promotion> promotions = new ConcurrentHashMap<>();

    private PromotionRepository() {}

    public Promotion save(Promotion entity) {
        String id = entity.getName();
        this.promotions.put(id, entity);
        return promotions.get(id);
    }

    public List<Promotion> saveAll(Iterable<Promotion> entities) {
        entities.forEach(this::save);
        return new ArrayList<>(promotions.values());
    }

    public Optional<Promotion> findByName(String name) {
        Promotion entity = this.promotions.get(name);
        return Optional.ofNullable(entity);
    }

    public List<Promotion> findAll() {
        return new ArrayList<>(this.promotions.values());
    }

    public void deleteAll() {
        this.promotions.clear();
    }
}
