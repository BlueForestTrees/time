package time.append;

        import com.google.inject.Guice;
        import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;
        import time.messaging.Messager;
        import time.messaging.Queue;

public class AppendMain {

    private static final Logger LOGGER = LogManager.getLogger(AppendMain.class);

    public static void main(final String[] args) throws Exception {
        final Messager messager = new Messager();

        final AppendRun appendRun = Guice.createInjector(new AppendModule(args)).getInstance(AppendRun.class);

        messager.when(Queue.APPEND, Append.class)
                .then(append -> {return appendRun.run(append);})
                .thenAccept(appendDone -> LOGGER.info(appendDone));

        messager.signal(Queue.APPEND, getMessage());

    }

    private static Append getMessage() {
        Append append = new Append();

        //append.setSource("D:\\dev\\time\\time\\data\\sources\\livres\\Adolf Hitler - Mon Combat - Mein Kampf.Ebook-Gratuit.co.epub");
        append.setSource("D:\\dev\\time\\time\\data\\sources\\timeline\\timeline.txt");

        return append;
    }

}
