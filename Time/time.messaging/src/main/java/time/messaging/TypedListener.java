package time.messaging;

import java.io.IOException;

@FunctionalInterface
public interface TypedListener<T> {
    void signal(T message) throws IOException;
}
