package store.commons.data.infrastructure;

import store.commons.lang.SystemException;

public class DataLoaderException extends SystemException {

    public DataLoaderException(String message) {
        super(message);
    }

    public DataLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
