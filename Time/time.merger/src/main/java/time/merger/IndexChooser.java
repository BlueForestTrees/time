package time.merger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfManager;
import time.conf.ConfEnum;

import java.io.IOException;

public class IndexChooser {
    private static final Logger LOGGER = LogManager.getLogger(IndexChooser.class);

    public Merge prepareMerge() throws IOException {
        final Merge merge = new ConfManager().get(ConfEnum.MERGER, Merge.class);
        final String currentWikiCrawlindexDir = new ConfManager().get(ConfEnum.WIKIWEB).getIndexDir();

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
