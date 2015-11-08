package time.transformer.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.repo.bean.Page;
import time.transformer.component.filter.PageFilter;
import time.transformer.component.reader.FinDuScanException;

@Service
public class FindUrlsService implements IModule {

    @Autowired
    PageReaderService pageReader;

    @Autowired
    PageFilter pageMemRepo;

    @Override
    public void onStart() {

    }

    @Override
    public long run(long pageCount) throws IOException, FinDuScanException {
        for (long i = 0; i < pageCount; i++) {
            Page page = pageReader.getNextPage();
            if (pageMemRepo.isValidNewPage(page)) {
                // TODO lucene
                // pageRepository.save(page);
                pageMemRepo.rememberThisPage(page);
            }
        }
        return pageCount;
    }

    @Override
    public void onEnd() {

    }
}
