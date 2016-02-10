package time.transformer;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.tool.chrono.Chrono;
import time.transformer.reader.FinDuScanException;

/**
 * Traite toutes les données par packets avec des logs intermédiaire.
 * Compte le nombre de pages traitées.
 * @author slim
 *
 */
@Component
public class Runner {
    private static final Logger LOG = LogManager.getLogger(Runner.class);

    @Autowired
    private Long pageSize;
    @Autowired
    private long pageTotal;
    @Autowired
    private long maxPhrasesToFetch;

    @Autowired
    private TransformerService module;

    public void run() throws IOException {
        LOG.info("run");

        long pageCount = 0;
        long phraseCount = 0;
        final Chrono chrono = new Chrono("Page");
        final Chrono fullChrono = new Chrono("Full");

        fullChrono.start();

        module.onStart();

        try {
            do {
                chrono.start();
                phraseCount += module.run(pageSize);
                pageCount += pageSize;
                chrono.stop();
                fullChrono.stop();
                LOG.debug("#" + pageCount + ", Total:" + fullChrono + ", Moy:" + fullChrono.toStringDividedBy(pageSize) + ", last:" + chrono + ", reste:" + fullChrono.getRemaining(pageCount, pageTotal) + " phrase#" + phraseCount);
            } while (maxPhrasesToFetch == -1L || (phraseCount < maxPhrasesToFetch));
        } catch (FinDuScanException e) {
            LOG.info("fin du scan (" + pageCount + " pages, " + phraseCount + " phrases");
        }

        module.onEnd();

        LOG.info("run end");
    }

}
