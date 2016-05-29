package time.messaging.console;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.ConfEnum;

import time.messaging.Messager;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessagingMain extends AbstractModule {

    private static final Logger LOGGER = LogManager.getLogger(MessagingMain.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        final Injector injector = Guice.createInjector(new MessagingMain());
        injector.getInstance(MessageLinker.class).on();
        injector.getInstance(MessageConsole.class).on();
    }

    @Provides
    public QueueLinks queueLinks() throws IOException {
        return new ConfManager().get(ConfEnum.LINKS, QueueLinks.class);
    }

    @Override
    protected void configure() {

    }
}
