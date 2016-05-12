package time.storage.tool;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import time.conf.Conf;
import time.domain.DatedPhrase;
import time.storage.store.PhraseStore;

public class PhraseStoreTest {

    private static final Logger LOG = LogManager.getLogger(PhraseStoreTest.class);

    private PhraseStore storage;
    private int logEvery = 1000;

    public PhraseStoreTest(){
    	Conf conf = new Conf();
    	conf.setIndexDir("C:/Time/data/testphrases");
        storage = new PhraseStore(conf);
    }
    
    @Test
    public void fillTest() throws IOException {
        storage.start();
        final long start = -10000;
        final long end = 10000;
        for (long l = start, count=0; l < end; l++, count++) {
            storage.store(getPhrase(l));
            if (count % logEvery == 0) {
                LOG.info(count + " phrases ajoutées");
            }
        }
        LOG.info(end-start + " phrases ajoutées");
        storage.end();
    }

    private DatedPhrase getPhrase(long l) {
        final DatedPhrase phrase = new DatedPhrase();
        phrase.setDate(l);
        phrase.setPageUrl("url " + l);
        phrase.setText("text " + l);
        return phrase;
    }

}
