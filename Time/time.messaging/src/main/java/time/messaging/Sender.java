package time.messaging;

import java.io.IOException;

public interface Sender<T> {
    void send(final T message) throws IOException;
}
