package time.web.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class OnContextStart{

    private static final Logger LOGGER = LogManager.getLogger(OnContextStart.class);

    private final Messager messager;

    public OnContextStart() throws IOException, TimeoutException {
        messager = new Messager();
    }

    @EventListener
    public void onContextRefreshed(final ContextRefreshedEvent contextStartedEvent) {
        LOGGER.info("onContextStarted");
        final AnnotationConfigWebApplicationContext webContext = (AnnotationConfigWebApplicationContext)contextStartedEvent.getApplicationContext();
        try {
            messager.when(Queue.WIKI_WEB_REFRESH).then(() -> webContext.refresh());
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @EventListener
    public void onContextClosed(final ContextClosedEvent contextClosedEvent) {
        messager.off();
    }

}
