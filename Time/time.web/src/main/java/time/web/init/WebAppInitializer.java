package time.web.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import time.messaging.Messager;
import time.messaging.Queue;
import time.messaging.SimpleConsumer;
import time.web.config.WebConfig;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WebAppInitializer extends AbstractDispatcherServletInitializer {

    private static final Logger LOGGER = LogManager.getLogger(WebAppInitializer.class);

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();

        webContext.register(WebConfig.class);

        try {
            final Messager messager = new Messager();
            messager.addReceiver(new SimpleConsumer() {
                @Override
                public Queue getQueue() {
                    return Queue.WIKI_WEB_REFRESH;
                }

                @Override
                public void message() {
                    webContext.refresh();
                    try {
                        messager.getSender(Queue.WIKI_WEB_REFRESH_END).signal();
                    } catch (IOException e) {
                        LOGGER.error(e);
                    }
                }
            });
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }

        return webContext;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

}