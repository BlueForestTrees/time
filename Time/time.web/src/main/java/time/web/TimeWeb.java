package time.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.domain.TimeWebConf;
import time.web.config.WebConfig;

import java.io.IOException;

public class TimeWeb {

    private static final Logger LOGGER = LogManager.getLogger(TimeWeb.class);

    public static void main(String[] args) {
        try {
            new TimeWeb().start();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private void start() throws Exception {
        final int port = new ConfManager().get(ConfEnum.TIMEWEB, TimeWebConf.class).getPort();
        LOGGER.info("address is localhost:"+port);
        final Server server = new Server(port);
        server.setHandler(getServletContextHandler(getContext()));
        server.start();
        server.join();
    }

    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
        contextHandler.addEventListener(new ContextLoaderListener(context));
        return contextHandler;
    }

    private static WebApplicationContext getContext() {
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        return context;
    }

}
