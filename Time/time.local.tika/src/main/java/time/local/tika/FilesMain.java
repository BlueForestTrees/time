package time.local.tika;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;

import java.io.IOException;

import static com.google.inject.Guice.createInjector;
import static time.messaging.Queue.LOCAL_TIKA;
import static time.messaging.Queue.MERGE;

public class FilesMain {

    private static final Logger LOGGER = LogManager.getLogger(FilesRun.class);

    public static void main(final String[] args) throws Exception {

        final Messager messager = new Messager();

        final FilesRun filesRun = createInjector(new FilesModule(args)).getInstance(FilesRun.class);

        messager.when(LOCAL_TIKA, () -> {
            filesRun.run();
            try {
                messager.signal(MERGE);
            } catch (IOException e) {
                LOGGER.error(e);
            }
        });

        messager.signal(LOCAL_TIKA);
    }

}