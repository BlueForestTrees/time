package time.append;

import com.google.inject.Guice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Meta;
import time.domain.IndexCreation;
import time.messaging.Messager;
import time.messaging.Queue;

import java.lang.reflect.InvocationTargetException;

public class IndexCreatorMain {

    private static final Logger LOGGER = LogManager.getLogger(IndexCreatorMain.class);

    public static void main(final String[] args) throws Exception {

        final Messager messager = new Messager();

        final IndexCreatorService indexCreatorService = Guice.createInjector(new IndexCreatorModule()).getInstance(IndexCreatorService.class);

        messager.when(Queue.META_CREATED, Meta.class)
                .then(meta -> {
                    try {
                        final IndexCreation indexCreation = indexCreatorService.run(meta);
                        messager.signal(Queue.INDEX_CREATED, indexCreation);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        LOGGER.error(e);
                    }
                });

    }

}
