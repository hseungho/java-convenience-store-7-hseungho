package store.commons.lang;

public class DataLoaderException extends RuntimeException {

    public DataLoaderException(String message) {
        super(message);
    }

    public DataLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
