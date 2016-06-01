package time.append;

        import com.google.inject.Guice;
        import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;
        import time.domain.Append;
        import time.domain.AppendDone;
        import time.messaging.Messager;
        import time.messaging.Queue;

        import java.io.IOException;

public class AppendMain {

    private static final Logger LOGGER = LogManager.getLogger(AppendMain.class);

    public static void main(final String[] args) throws Exception {
        final Messager messager = new Messager();

        final AppendRun appendRun = Guice.createInjector(new AppendModule(args)).getInstance(AppendRun.class);

        messager.when(Queue.APPEND, Append.class)
                .then(append -> appendRun.run(append))
                .thenAccept(appendDone -> {
                    try {
                        messager.signal(Queue.APPEND_DONE, appendDone);
                    } catch (IOException e) {
                        LOGGER.error(e);
                    }
                });

        messager.signal(Queue.APPEND, getMessage());

    }

    private static Append getMessage() {
        final Append append = new Append();

        append.setSource("${TIME_HOME}/sources/tika/timeline.txt");
        append.setUrl("timeline.fr");
        append.setTitle("Timeline - Le jeu de société");

        return append;
    }

}
