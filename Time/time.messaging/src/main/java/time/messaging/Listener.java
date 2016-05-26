package time.messaging;

import java.io.IOException;

@FunctionalInterface
public interface Listener {
    void signal() throws IOException;
}
