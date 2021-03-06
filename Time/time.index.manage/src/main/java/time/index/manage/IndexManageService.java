package time.index.manage;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.domain.IndexCreation;
import time.domain.Merge;
import time.domain.TimeWebConf;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class IndexManageService {

    private static final Logger LOGGER = LogManager.getLogger(IndexManageService.class);

    private final IndexService indexService;
    private final BlueRedSwitcher indexChooser;
    private final Messager messager;

    public IndexManageService(String[] args) throws IOException, TimeoutException, ArgumentParserException {
        LOGGER.info("IndexManageMain()");
        indexService = new IndexService();
        indexChooser = new BlueRedSwitcher();

        if(args.length > 0){
            final Queue command = Queue.valueOf(args[0]);
            if(command == Queue.MERGE){
                onMergeSignal();
            }
            messager = null;
        }else {
            messager = new Messager();
            installMessager();
        }
    }

    private void installMessager() throws IOException {
        messager.when(Queue.MERGE, this::onMergeSignal)
                .when(Queue.INDEX_CREATED, IndexCreation.class, this::onIndexCreatedSignal);
    }

    private void onIndexCreatedSignal(IndexCreation indexCreation) {
        try {
            if(indexCreation.isOverwriteOccurs()){
                doMerge();
            }else {
                doAppend(indexCreation);
            }
            messager.signal(Queue.TIME_WEB_REFRESH);
        }catch(Exception e){
            LOGGER.error(e);
        }
    }

    private void onMergeSignal() {
        try {
            doMerge();
            messager.signal(Queue.TIME_WEB_REFRESH);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private void doAppend(IndexCreation indexCreation) throws IOException {
        LOGGER.info("doAppend");
        indexService.append(indexCreation);
    }

    private void doMerge() throws IOException {
        LOGGER.info("doMerge");
        final Merge merge = indexChooser.prepareMerge();
        indexService.merge(merge);
        updateWikiWebConf(merge);
    }

    private void updateWikiWebConf(final Merge merge) {
        LOGGER.info("set {}.indexDir: {}", ConfEnum.TIMEWEB.getPath(), merge.getMergedIndexDir());
        try {
            final TimeWebConf conf = new ConfManager().get(ConfEnum.TIMEWEB, TimeWebConf.class);
            conf.setIndexDir(merge.getMergedIndexDir());
            new ConfManager().set(ConfEnum.TIMEWEB, conf);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
