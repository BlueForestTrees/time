package time.messaging;

public interface SimpleConsumer {
    Queue getQueue();
    void message();
}
