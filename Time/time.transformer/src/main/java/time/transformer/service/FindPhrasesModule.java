package time.transformer.service;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import time.repo.bean.Page;
import time.transformer.component.filter.PageFilter;
import time.transformer.component.reader.FinDuScanException;
import time.transformer.repo.PageRepository;
import time.transformer.repo.PhraseRepository;
import time.transformer.tool.handler.PhraseHandler;

@Service
public class FindPhrasesModule implements IModule {

    private static final Logger log = LogManager.getLogger(FindPhrasesModule.class);

    @Autowired
    PageRepository pageRepository;

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    PageReaderService pageReader;

    @Autowired
    PageFilter pageFilter;

    @Autowired
    PhraseHandler phraseHandler;

    @Transactional
    public void onStart() {
        log.info("onStart()");
        pageRepository.deleteAll();
        phraseRepository.deleteAll();
    }

    @Transactional
    public long run(long pageCount) throws IOException, FinDuScanException {
        System.out.println();
        System.out.println();
        System.out.println("NEW DATA PAGE");
        System.out.println();
        System.out.println();
        long phraseCount = 0;
        for (long i = 0; i < pageCount; i++) {
            Page page = pageReader.getNextPage();
            if (pageFilter.isValidPage(page)) {
                if (pageFilter.isNewPage(page)) {
                    page = pageRepository.save(page);
                } else {
                    page.setId(pageRepository.getIdByUrl(page.getUrl()));
                }

                phraseCount += phraseHandler.handle(page);

                pageFilter.rememberThisPage(page);
            }
        }
        return phraseCount;
    }

    public void onEnd() {
        log.info("onEnd()");
    }

}
