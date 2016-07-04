package time.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.domain.TimeWebConf;
import time.web.config.WebConfig;
import org.apache.logging.log4j.Level;
import org.eclipse.jetty.util.log.AbstractLogger;

import java.io.IOException;

/**
 * Java app with jetty + a spring context.
 */
public class TimeWeb {

    private static final Logger LOGGER = LogManager.getLogger(TimeWeb.class);

    public static void main(String[] args) {
        try {
            new TimeWeb().start();
        } catch (Exception e) {
            LOGGER.error(e);
            System.exit(-1);
        }
    }

    private void start() throws Exception {
        final int port = new ConfManager().get(ConfEnum.TIMEWEB, TimeWebConf.class).getPort();
        LOGGER.info("address is localhost:"+port);
        Log.setLog(new Jetty2Log4j2Bridge("Jetty"));
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

    private class Jetty2Log4j2Bridge extends AbstractLogger
    {
        private org.apache.logging.log4j.Logger logger;
        private String name;

        Jetty2Log4j2Bridge(String name)
        {
            this.name = name;
            logger = LogManager.getLogger(name);
        }

        @Override
        protected org.eclipse.jetty.util.log.Logger newLogger(String fullname)
        {
            return new Jetty2Log4j2Bridge(fullname);
        }

        public String getName()
        {
            return name;
        }

        public void warn(String msg, Object... args)
        {
            logger.warn(msg, args);
        }

        public void warn(Throwable thrown)
        {
            logger.catching(Level.WARN, thrown);
        }

        public void warn(String msg, Throwable thrown)
        {
            logger.warn(msg, thrown);
        }

        public void info(String msg, Object... args)
        {
            logger.info(msg, args);
        }

        public void info(Throwable thrown)
        {
            logger.catching(Level.INFO, thrown);
        }

        public void info(String msg, Throwable thrown)
        {
            logger.info(msg, thrown);
        }

        public boolean isDebugEnabled()
        {
            return logger.isDebugEnabled();
        }

        public void setDebugEnabled(boolean enabled)
        {
            warn("setDebugEnabled not implemented", null, null);
        }

        public void debug(String msg, Object... args)
        {
            logger.debug(msg, args);
        }

        public void debug(Throwable thrown)
        {
            logger.catching(Level.DEBUG, thrown);
        }

        public void debug(String msg, Throwable thrown)
        {
            logger.debug(msg, thrown);
        }

        public void ignore(Throwable ignored)
        {
            logger.catching(Level.TRACE, ignored);
        }
    }

}
