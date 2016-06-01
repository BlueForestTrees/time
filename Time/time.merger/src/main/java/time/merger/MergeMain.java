package time.merger;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.domain.Append;
import time.domain.AppendDone;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MergeMain {

    private static final Logger LOGGER = LogManager.getLogger(MergeMain.class);

    private final IndexService indexService;
    private final IndexChooser indexChooser;
    private final Messager messager;

    public static void main(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        new MergeMain(args);
    }

    public MergeMain(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        LOGGER.info("MergeMain()");
        indexService = new IndexService();
        indexChooser = new IndexChooser();
        messager = new Messager();

        messager.when(Queue.MERGE)
                .then(()->{
                    final Merge merge = indexChooser.prepareMerge();
                    indexService.merge(merge);
                    updateWikiWebConf(merge);
                    messager.signal(Queue.WIKI_WEB_REFRESH);
                });

        messager.when(Queue.APPEND, AppendDone.class)
                .then((appendDone)-> indexService.append(appendDone));
    }

    private void updateWikiWebConf(Merge merge) {
        LOGGER.info("set wikiWeb.indexDir: {}", merge.getMergedIndexDir());
        try {
            new ConfManager().update(ConfEnum.WIKIWEB, conf -> conf.setIndexDir(merge.getMergedIndexDir()));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public String toString() {
        return "MergeMain";
    }
}
