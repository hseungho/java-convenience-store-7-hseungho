package store.commons.data.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class SimpleRepository<T, ID extends Comparable<ID>> extends AbstractRepository<T, ID> {

    @Override
    public T save(T entity) {
        ID id = this.getId(entity);
        this.storage.put(id, entity);
        return this.storage.get(id);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        List<T> result = new ArrayList<>();
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public List<T> findAll() {
        return this.storage.values().stream()
                .sorted(Comparator.comparing(this::getId))
                .toList();
    }

    @Override
    public Optional<T> findById(ID id) {
        T entity = this.storage.get(id);
        return Optional.ofNullable(entity);
    }

    @Override
    public boolean existsById(ID id) {
        return this.storage.containsKey(id);
    }

    @Override
    public void deleteAll() {
        this.storage.clear();
    }

    public void deleteById(ID id) {
        this.storage.remove(id);
    }
}
