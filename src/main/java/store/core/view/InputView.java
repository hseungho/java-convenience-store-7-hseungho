package store.core.view;

public interface InputView<T, S> extends View<T> {

    S displayWithInput(T content);

}
