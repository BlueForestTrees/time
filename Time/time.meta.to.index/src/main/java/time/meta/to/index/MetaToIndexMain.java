package time.meta.to.index;

import com.google.inject.Guice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Meta;
import time.domain.IndexCreation;
import time.messaging.Messager;
import time.messaging.Queue;

import java.lang.reflect.InvocationTargetException;

public class MetaToIndexMain {

    private static final Logger LOGGER = LogManager.getLogger(MetaToIndexMain.class);

    public static void main(final String[] args) {

        try {
            final Messager messager = new Messager();
            final MetaToIndexService metaToIndexService = Guice.createInjector(new MetaToIndexModule()).getInstance(MetaToIndexService.class);
            messager.when(Queue.META_CREATED, Meta.class)
                    .then(meta -> {
                        try {
                            final IndexCreation indexCreation = metaToIndexService.run(meta);
                            messager.signal(Queue.INDEX_CREATED, indexCreation);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            LOGGER.error(e);
                        }
                    });
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

}
