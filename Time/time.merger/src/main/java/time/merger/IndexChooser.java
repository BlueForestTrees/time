package time.merger;

import time.conf.ConfManager;
import time.conf.Confs;

import java.io.IOException;

public class IndexChooser {
    public Merge prepareMerge() throws IOException {
        final Merge merge = ConfManager.get(Confs.MERGER, Merge.class);
        final String currentWikiCrawlindexDir = ConfManager.get(Confs.WIKIWEB).getIndexDir();

        if(currentWikiCrawlindexDir.endsWith("red")){
            merge.setMergedIndexDir("${TIME_HOME}/indexes/merged/blue");
        }else{
            merge.setMergedIndexDir("${TIME_HOME}/indexes/merged/red");
        }
        return merge;
    }
}
