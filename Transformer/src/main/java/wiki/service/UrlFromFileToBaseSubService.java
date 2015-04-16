package wiki.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.component.reader.FinDuScanException;
import wiki.entity.Page;
import wiki.repo.PageRepository;

@Service
public class UrlFromFileToBaseSubService {

	private static final Logger logger = LogManager.getLogger(UrlFromFileToBaseSubService.class);
	
	private List<String> urlsLowerCase = new ArrayList<String>();

	@Autowired
	PageRepository pageRepository;
	
	@Autowired
	PageReaderService pageReader;

	@Transactional
	public void run(long pageCount) throws IOException, FinDuScanException {
		for (long i = 0; i < pageCount; i++) {
			Page page;
			page = pageReader.getNextPage();
			if(!urlsLowerCase.contains(page.getUrl().toLowerCase())){
				pageRepository.save(page);
				urlsLowerCase.add(page.getUrl().toLowerCase());
			}
		}
	}
}
