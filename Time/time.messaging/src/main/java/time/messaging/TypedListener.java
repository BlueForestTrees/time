package time.messaging;

import java.io.IOException;

@FunctionalInterface
public interface TypedListener<T,U> {
    U signal(T message) throws IOException;
}
