package time.local.tika;

import com.google.inject.Guice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;

public class FilesMain {

    private static final Logger LOGGER = LogManager.getLogger(FilesRun.class);

    public static void main(final String[] args) throws Exception {

        final Messager messager = new Messager();

        final FilesRun filesRun = Guice.createInjector(new FilesModule(args)).getInstance(FilesRun.class);

        messager.when(Queue.FULL_INDEX_RELOAD, () -> {
            filesRun.run();
            try {
                messager.signal(Queue.MERGE);
            } catch (IOException e) {
                LOGGER.error(e);
            }
        });

        messager.signal(Queue.FULL_INDEX_RELOAD);
    }

}