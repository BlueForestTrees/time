package wiki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.entity.Page;
import wiki.repo.PageRepository;

@Service
public class PageService{
	
	@Autowired
	PageRepository pageRepository;
	
	//Integer pagesMaxSize = 100;
	//AbstractCollection<Page> pages = new ArrayList<Page>();
	
	public void save(Page page) {
		
		pageRepository.save(page);
		
		//pages.add(page);
//		if(pages.size() > pagesMaxSize){
//			pageRepository.save(pages);
//			pages = new ArrayList<Page>();
//		}
	}

}
