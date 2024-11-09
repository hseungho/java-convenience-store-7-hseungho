package store.commons.data.repository;

import store.commons.lang.SystemException;

public class PersistenceException extends SystemException {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

}
