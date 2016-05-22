package time.merger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;
import time.messaging.Queue;
import time.messaging.Consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MergeMain extends Consumer<Merge> {

    private static final Logger LOGGER = LogManager.getLogger(MergeMain.class);

    final MergeService mergeService;

    public static void main(String[] args) throws IOException, TimeoutException {
        new MergeMain();
    }

    public MergeMain() throws IOException, TimeoutException {
        LOGGER.info("MergeMain()");
        mergeService = new MergeService();
        new Messager().addReceiver(this, Merge.class);
    }

    @Override
    public Queue getQueue() {
        return Queue.MERGE;
    }

    @Override
    public void message(final Merge merge) {
        LOGGER.info("received message " + merge);
        try {
            mergeService.merge(merge);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public String toString() {
        return "MergeMain";
    }
}
