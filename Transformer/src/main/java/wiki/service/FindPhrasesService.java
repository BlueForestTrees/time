package wiki.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.component.reader.FinDuScanException;
import wiki.component.util.PageMemRepo;
import wiki.entity.Page;
import wiki.repo.PageRepository;
import wiki.repo.PhraseRepository;
import wiki.tool.handler.PhraseHandler;

@Service
public class FindPhrasesService {
	private static final Logger logger = LogManager.getLogger(FindPhrasesService.class);

	@Autowired
	PageRepository pageRepository;

	@Autowired
	PhraseRepository phraseRepository;

	@Autowired
	PageReaderService pageReader;

	@Autowired
	PageMemRepo pageMemRepo;

	@Autowired
	PhraseHandler phraseHandler;

	public void onStart() {

	}

	@Transactional
	public void run(long pageCount) throws IOException, FinDuScanException {
		for (long i = 0; i < pageCount; i++) {
			Page page = pageReader.getNextPage();
			if (pageMemRepo.isPageValid(page)) {
					//page.setId(pageRepository.getIdByUrl(page.getUrl()));

					phraseHandler.handle(page);

					pageMemRepo.rememberThisPage(page);
			}
		}
	}

	public void onEnd() {

	}

}
