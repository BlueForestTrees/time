package wiki.service;

import java.io.IOException;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.component.reader.FinDuScanException;
import wiki.entity.Page;
import wiki.repo.PageRepository;

@Service
public class UrlFromFileToBaseSubService {

	private static final int URL_MAX_LENGTH = 255;

	private HashSet<String> urlsLowerCase = new HashSet<String>();

	@Autowired
	PageRepository pageRepository;
	
	@Autowired
	PageReaderService pageReader;

	@Transactional
	public void run(long pageCount) throws IOException, FinDuScanException {
		for (long i = 0; i < pageCount; i++) {
			Page page = pageReader.getNextPage();
			if(isThisPageNew(page) && thisPageUrlIsnotTooLong(page)){
				pageRepository.save(page);
				rememberThisPage(page);
			}
		}
	}

	private boolean thisPageUrlIsnotTooLong(Page page) {
		return page.getUrl().length() <= URL_MAX_LENGTH;
	}

	private void rememberThisPage(Page page) {
		urlsLowerCase.add(page.getUrl().toLowerCase());
	}

	private boolean isThisPageNew(Page page) {
		return !urlsLowerCase.contains(page.getUrl().toLowerCase());
	}
}
