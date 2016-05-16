package time.messaging;

public abstract class Consumer<T> implements SimpleConsumer {
    public void message(){
        throw new RuntimeException("Consumer<T>.message() must never be called");
    }
    public abstract void message(final T message);
}
