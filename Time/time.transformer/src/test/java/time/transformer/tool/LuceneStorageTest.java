package time.transformer.tool;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import time.repo.bean.FullPhrase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LuceneStorageConfig.class })
public class LuceneStorageTest {

    private static final Logger LOG = LogManager.getLogger(LuceneStorageTest.class);

    @Autowired
    LuceneStorage storage;

    private int logEvery = 1000;

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

    private FullPhrase getPhrase(long l) {
        final FullPhrase phrase = new FullPhrase();
        phrase.setDate(l);
        phrase.setText(String.valueOf(l));
        return phrase;
    }

}
