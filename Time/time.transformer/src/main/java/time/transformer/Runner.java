package time.transformer;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.tool.chrono.Chrono;
import time.transformer.reader.FinDuScanException;

/**
 * Traite toutes les données par packets avec des logs intermédiaire.
 * Compte le nombre de pages traitées.
 * @author slim
 *
 */
public class Runner {
    private static final Logger LOG = LogManager.getLogger(Runner.class);

    private Long pageSize;
    private long pageTotal;
    private long maxPhrasesToFetch;
    private TransformerService transformer;
    
    @Inject
    public Runner(@Named("conf") Conf conf, final TransformerService transformer){
    	this.pageSize = conf.getPageSize();
    	this.pageTotal = conf.getPageTotal();
    	this.maxPhrasesToFetch = conf.getMaxPhrasesToFetch();
    	this.transformer = transformer;
    }

    public void run() throws IOException {
        LOG.info("run");

        long pageCount = 0;
        long phraseCount = 0;
        final Chrono chrono = new Chrono("Page");
        final Chrono fullChrono = new Chrono("Full");

        fullChrono.start();

        transformer.onStart();

        try {
            do {
                chrono.start();
                phraseCount += transformer.run(pageSize);
                pageCount += pageSize;
                chrono.stop();
                fullChrono.stop();
                LOG.debug("#" + pageCount + ", Total:" + fullChrono + ", Moy:" + fullChrono.toStringDividedBy(pageSize) + ", last:" + chrono + ", reste:" + fullChrono.getRemaining(pageCount, pageTotal) + " phrase#" + phraseCount);
            } while (maxPhrasesToFetch == -1L || (phraseCount < maxPhrasesToFetch));
        } catch (FinDuScanException e) {
            LOG.info("fin du scan (" + pageCount + " pages, " + phraseCount + " phrases");
        }

        transformer.onEnd();

        LOG.info("run end");
    }

}
