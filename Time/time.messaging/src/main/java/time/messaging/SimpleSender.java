package time.messaging;

import java.io.IOException;

public interface SimpleSender {
    void signal() throws IOException;
}
