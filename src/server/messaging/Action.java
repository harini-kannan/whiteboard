package server.messaging;

public interface Action<T extends ActionQueue<?>> {
    public void perform(T t);
}
