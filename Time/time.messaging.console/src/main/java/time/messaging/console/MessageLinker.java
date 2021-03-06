package time.messaging.console;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.function.Consumer;

public class MessageLinker {

    private static final Logger LOGGER = LogManager.getLogger(MessageLinker.class);

    final Messager messager;
    final QueueLinks links;

    @Inject
    public MessageLinker(final Messager messager, final QueueLinks links) {
        this.messager = messager;
        this.links = links;
    }

    public void on() {
        links.getQueueLinks().forEach(this::linkFromWithTo);
    }

    private void linkFromWithTo(final QueueLinks.QueueLink link) {
        try {
            messager.when(link.getFrom(),() -> messager.signal(link.getTo()));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
