package time.merger;

import time.conf.ConfManager;
import time.conf.ConfEnum;

import java.io.IOException;

public class IndexChooser {
    public Merge prepareMerge() throws IOException {
        final Merge merge = new ConfManager().get(ConfEnum.MERGER, Merge.class);
        final String currentWikiCrawlindexDir = new ConfManager().get(ConfEnum.WIKIWEB).getIndexDir();

        if(currentWikiCrawlindexDir.endsWith("red")){
            merge.setMergedIndexDir("${TIME_HOME}/indexes/merged/blue");
        }else{
            merge.setMergedIndexDir("${TIME_HOME}/indexes/merged/red");
        }
        return merge;
    }
}
