package store.commons.data.repository;

public class EntityNotFoundIdException extends PersistenceException {

    public EntityNotFoundIdException(String message) {
        super(message);
    }

    public EntityNotFoundIdException(String message, Throwable cause) {
        super(message, cause);
    }

}
