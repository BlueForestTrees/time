package time.transformer.tool;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import time.conf.Conf;
import time.repo.bean.Phrase;
import time.transformer.storage.LuceneStorage;

public class LuceneStorageTest {

    private static final Logger LOG = LogManager.getLogger(LuceneStorageTest.class);

    private LuceneStorage storage;
    private int logEvery = 1000;

    public LuceneStorageTest(){
    	Conf conf = new Conf();
    	conf.setIndexPath("C:/Time/data/testphrases");
        storage = new LuceneStorage(conf);
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

    private Phrase getPhrase(long l) {
        final Phrase phrase = new Phrase();
        phrase.setDate(l);
        phrase.setText(String.valueOf(l));
        return phrase;
    }

}
