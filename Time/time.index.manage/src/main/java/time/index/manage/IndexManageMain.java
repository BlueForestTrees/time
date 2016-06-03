package time.index.manage;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.domain.IndexCreation;
import time.domain.Merge;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class IndexManageMain {

    private static final Logger LOGGER = LogManager.getLogger(IndexManageMain.class);

    private final IndexService indexService;
    private final BlueRedSwitcher indexChooser;
    private final Messager messager;

    public static void main(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        new IndexManageMain();
    }

    public IndexManageMain() throws IOException, TimeoutException, ArgumentParserException {
        LOGGER.info("IndexManageMain()");
        indexService = new IndexService();
        indexChooser = new BlueRedSwitcher();
        messager = new Messager();

        messager.when(Queue.MERGE)
                .then(()->{
                    final Merge merge = indexChooser.prepareMerge();
                    indexService.merge(merge);
                    updateWikiWebConf(merge);
                    messager.signal(Queue.WIKI_WEB_REFRESH);
                });

        messager.when(Queue.INDEX_CREATED, IndexCreation.class)
                .then((appendDone) -> {
                    indexService.append(appendDone);
                    messager.signal(Queue.WIKI_WEB_REFRESH);
                });
    }

    private void updateWikiWebConf(final Merge merge) {
        LOGGER.info("set wikiWeb.indexDir: {}", merge.getMergedIndexDir());
        try {
            new ConfManager().update(ConfEnum.TIMEWEB, conf -> conf.setIndexDir(merge.getMergedIndexDir()));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public String toString() {
        return "IndexManageMain";
    }
}
