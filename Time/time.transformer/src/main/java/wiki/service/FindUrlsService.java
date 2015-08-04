package wiki.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.component.reader.FinDuScanException;
import wiki.component.util.PageMemRepo;
import time.repo.Page;
import wiki.repo.PageRepository;

@Service
public class FindUrlsService implements IService{

	@Autowired
	PageRepository pageRepository;
	
	@Autowired
	PageReaderService pageReader;
	
	@Autowired
	PageMemRepo pageMemRepo;

	@Override
	public void onStart() {
		
	}
	
	@Override
	@Transactional
	public void run(long pageCount) throws IOException, FinDuScanException {
		for (long i = 0; i < pageCount; i++) {
			Page page = pageReader.getNextPage();
			if(pageMemRepo.isPageValid(page)){
				pageRepository.save(page);
				pageMemRepo.rememberThisPage(page);
			}
		}
	}


	@Override
	public void onEnd() {
		
	}
}
