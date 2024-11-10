package store.commons.data.repository;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRepository<T, ID extends Comparable<ID>> implements Repository<T, ID> {

    protected final ConcurrentHashMap<ID, T> storage;

    private static Long generateValue = 0L;

    protected AbstractRepository() {
        this.storage = new ConcurrentHashMap<>();
    }

    protected ID getId(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            return getIdIfPresent(entity, field);
        }
        throw new EntityNotFoundIdException(entity.getClass().getName() + " 엔티티에 선언된 '@Id'가 없습니다.");
    }

    private ID getIdIfPresent(T entity, Field field) {
        if (field.isAnnotationPresent(Id.class)) {
            return findId(entity, field);
        }
        throw new PersistenceException(entity.getClass().getName() + " 엔티티의 Id 필드를 찾을 수 없습니다.");
    }

    @SuppressWarnings("unchecked")
    private ID findId(T entity, Field field) {
        try {
            field.setAccessible(true);
            ID id = (ID) field.get(entity);
            if (id != null) return id;
            id = setIdIfRequiredGenerateValue(entity, field);
            return id;
        } catch (IllegalAccessException e) {
            throw new PersistenceException(entity.getClass().getName() + "엔티티의 Id 필드를 가져오는 과정에서 오류가 발생하였습니다.");
        }
    }

    private boolean isRequiredGenerateValueId(Field idField) {
        return idField.isAnnotationPresent(GenerateValue.class) &&
                Number.class.isAssignableFrom(idField.getType());
    }

    @SuppressWarnings("unchecked")
    private ID setIdIfRequiredGenerateValue(T entity, Field field) throws IllegalAccessException {
        if (isRequiredGenerateValueId(field)) {
            ID id = (ID) generateId();
            field.setAccessible(true);
            field.set(entity, id);
            field.setAccessible(false);
            return id;
        }
        throw new PersistenceException(entity.getClass().getName() + " 엔티티의 Id를 생성하는 과정에서 오류가 발생하였습니다.");
    }

    private Long generateId() {
        synchronized (generateValue) {
            return ++generateValue;
        }
    }
}
