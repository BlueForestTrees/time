package time.messaging.console;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;

public class MessageLinker {

    private static final Logger LOGGER = LogManager.getLogger(MessageLinker.class);

    final Messager messager;
    final QueueLinks links;

    @Inject
    public MessageLinker(final Messager messager, final QueueLinks links) {
        this.messager = messager;
        this.links = links;
    }

    public void on(){

        try {
            messager.signal(Queue.WIKI_CRAWL_END);
        } catch (IOException e) {
            LOGGER.error(e);
        }

        links.getQueueLinks().forEach(link -> {
            try {
                messager.when(link.getFrom()).then(() -> messager.signal(link.getTo()));
            } catch (IOException e) {
                LOGGER.error(e);
            }
        });

    }

    public void off(){
        //TODO Link.OFF
    }
}
