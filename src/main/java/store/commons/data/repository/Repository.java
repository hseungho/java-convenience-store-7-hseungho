package store.commons.data.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID extends Comparable<ID>> {

    T save(T entity);

    List<T> saveAll(Iterable<T> entities);

    List<T> findAll();

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    void deleteAll();

    void deleteById(ID id);

}
