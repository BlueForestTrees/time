package time.messaging;

import java.io.IOException;

@FunctionalInterface
public interface EventListener {
    void signal() throws IOException;
}
