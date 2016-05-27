package time.merger;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MergeMain {

    private static final Logger LOGGER = LogManager.getLogger(MergeMain.class);

    private final MergeService mergeService;
    private final IndexChooser indexChooser;

    public static void main(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        new MergeMain(args);
    }

    public MergeMain(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        LOGGER.info("MergeMain()");
        mergeService = new MergeService();
        indexChooser = new IndexChooser();
        final Messager messager = new Messager();
        messager.when(Queue.MERGE)
                .then(()->{
                    LOGGER.info("received signal " + Queue.MERGE);
                    try {
                        final Merge merge = indexChooser.prepareMerge();
                        mergeService.merge(merge);
                        new ConfManager().update(ConfEnum.WIKICRAWL, conf -> conf.setMergedIndexDir(merge.getMergedIndexDir()));
                        new Messager().signal(Queue.WIKI_WEB_REFRESH);
                    } catch (TimeoutException | IOException e) {
                        LOGGER.error(e);
                    }
                });
    }

    @Override
    public String toString() {
        return "MergeMain";
    }
}
