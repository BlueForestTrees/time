package wiki.crawler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import wiki.entity.Page;

@Component
public class CrawlerGroup implements ICrawler {

	public static CrawlerGroup crawlerGroup;
	
	public String getBaseUrl() {
		return "http://fr.wikipedia.org/wiki";
		//return "http://192.168.0.43/test";
	}
	
	@Resource(name="crawlers")
	private List<ICrawler> crawlers;
		
	public void visit(Page page) {  
		for(ICrawler crawler : crawlers){
			crawler.visit(page);
		}
	}

}
