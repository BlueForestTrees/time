package time.merger;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.Confs;
import time.messaging.Messager;
import time.messaging.Queue;
import time.messaging.SimpleConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MergeMain implements SimpleConsumer {

    private static final Logger LOGGER = LogManager.getLogger(MergeMain.class);

    final MergeService mergeService;

    public static void main(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        new MergeMain(args);
    }

    public MergeMain(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        LOGGER.info("MergeMain()");
        mergeService = new MergeService();
        new Messager().addReceiver(this);
    }

    @Override
    public Queue getQueue() {
        return Queue.MERGE;
    }

    @Override
    public void message() {
        LOGGER.info("received signal " + Queue.MERGE);
        try {
            mergeService.merge(merge);


            new Messager().getSender(Queue.START_WIKI_CRAWL).signal();
        } catch (TimeoutException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public String toString() {
        return "MergeMain";
    }
}
