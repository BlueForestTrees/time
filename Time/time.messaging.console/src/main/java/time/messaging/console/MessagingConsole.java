package time.messaging.console;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.ConfEnum;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessagingConsole extends AbstractModule {

    private static final Logger LOGGER = LogManager.getLogger(MessagingConsole.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        Guice.createInjector(new MessagingConsole()).getInstance(MessageLinker.class).on();
    }

    @Provides
    public QueueLinks queueLinks() throws IOException {
        return new ConfManager().get(ConfEnum.LINKS, QueueLinks.class);
    }

    @Override
    protected void configure() {

    }
}
