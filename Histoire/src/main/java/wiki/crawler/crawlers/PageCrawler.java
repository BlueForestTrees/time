package wiki.crawler.crawlers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.crawler.ICrawler;
import wiki.entity.Page;
import wiki.service.PageService;

@Service
public class PageCrawler implements ICrawler{
	
	protected static Logger log = Logger.getLogger(PageCrawler.class.getName());
	
	@Autowired
	private PageService pageService;
	
	@Override
	public void visit(Page page) {
		pageService.save(page);
	}
}
