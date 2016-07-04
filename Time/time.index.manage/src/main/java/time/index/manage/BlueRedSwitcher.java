package time.index.manage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.domain.Merge;
import time.domain.TimeWebConf;

import java.io.IOException;

public class BlueRedSwitcher {
    private static final Logger LOGGER = LogManager.getLogger(BlueRedSwitcher.class);

    public Merge prepareMerge() throws IOException {
        final Merge merge = new Merge();
        merge.setMergeableIndexesDir("${TIME_HOME}/indexes/mergeables");

        final String currentWikiCrawlindexDir = new ConfManager().get(ConfEnum.TIMEWEB, TimeWebConf.class).getIndexDir();

        LOGGER.info("wiki.web is {}", currentWikiCrawlindexDir);

        if(currentWikiCrawlindexDir.endsWith("red")){
            LOGGER.info("go to blue");
            merge.setMergedIndexDir("${TIME_HOME}/indexes/merged/blue");
        }else{
            LOGGER.info("go to red");
            merge.setMergedIndexDir("${TIME_HOME}/indexes/merged/red");
        }
        return merge;
    }
}
