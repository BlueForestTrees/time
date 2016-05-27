package time.web.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import time.messaging.Messager;
import time.messaging.Queue;
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
            messager.when(Queue.WIKI_WEB_REFRESH).then(() -> {
                webContext.refresh();
                try {
                    messager.signal(Queue.WIKI_WEB_REFRESH_END);
                } catch (IOException e) {
                    LOGGER.error(e);
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
        return new String[]{"/"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

}