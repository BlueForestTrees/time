package time.transformer.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.repo.bean.Page;
import time.transformer.component.filter.PageFilter;
import time.transformer.component.reader.FinDuScanException;

@Service
public class CountLiensService implements IModule {
    private static final Logger logger = LogManager.getLogger(CountLiensService.class);

    @Autowired
    private String path;

    @Autowired
    PageReaderService pageReader;

    @Autowired
    PageFilter pageMemRepo;

    private Map<Long, Long> liensCount = new HashMap<Long, Long>();

    @Override
    public void onStart() {

    }

    @Override
    public long run(long pageCount) throws IOException, FinDuScanException {
        logger.info("run");

        long nbPages = 0;
        long nbLiens = 0;
        long nbLiensTrouves = 0;

        for (long i = 0; i < pageCount; i++) {
            Page fromPage = pageReader.getNextPage();
            if (pageMemRepo.isValidNewPage(fromPage)) {
                for (String lienOut : fromPage.getLiens()) {
                    //TODO replace lucene
                    //Long toId = pageRepository.getIdByUrl(lienOut);
                    /*if (toId != null) {
                        Long toIdCount = liensCount.get(toId);
                        if (toIdCount == null) {
                            liensCount.put(toId, 1L);
                        } else {
                            toIdCount++;
                        }
                        nbLiensTrouves++;
                    }*/
                    nbLiens++;
                }
                pageMemRepo.rememberThisPage(fromPage);
                nbPages++;
            }
        }
        logger.info("run end, " + nbPages + " pages, liens " + nbLiensTrouves + "/" + nbLiens);
        return nbPages;
    }

    @Override
    public void onEnd() {
        logger.info("save liensCount...");
        liensCount.forEach(new BiConsumer<Long, Long>() {
            @Override
            public void accept(Long toId, Long toIdCount) {
                //TODO lucene
                //pageRepository.updateNbLiensIn(toId, toIdCount);
            }
        });
        logger.info("save liensCount done");
    }
}
