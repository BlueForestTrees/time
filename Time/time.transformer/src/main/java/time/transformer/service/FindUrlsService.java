package time.transformer.service;

import java.io.IOException;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import time.repo.bean.Page;
import time.transformer.component.filter.PageFilter;
import time.transformer.component.reader.FinDuScanException;
import time.transformer.repo.PageRepository;

@Service
public class FindUrlsService implements IModule{

	@Autowired
	PageRepository pageRepository;
	
	@Autowired
	PageReaderService pageReader;
	
	@Autowired
	PageFilter pageMemRepo;

	@Override
	public void onStart() {
		
	}
	
	@Override
	@Transactional
	public long run(long pageCount) throws IOException, FinDuScanException {
		for (long i = 0; i < pageCount; i++) {
			Page page = pageReader.getNextPage();
			if(pageMemRepo.isValidNewPage(page)){
				pageRepository.save(page);
				pageMemRepo.rememberThisPage(page);
			}
		}
		return pageCount;
	}


	@Override
	public void onEnd() {
		
	}
}
