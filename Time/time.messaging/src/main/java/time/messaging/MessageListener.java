package time.messaging;

import java.io.IOException;

@FunctionalInterface
public interface MessageListener<T> {
    void signal(T message) throws IOException;
}
