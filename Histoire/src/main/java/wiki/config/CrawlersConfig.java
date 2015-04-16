package wiki.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import wiki.crawler.ICrawler;
import wiki.crawler.crawlers.PageCrawler;

@Configuration
@ComponentScan("wiki.crawler.crawlers")
public class CrawlersConfig {

	@Autowired
	private PageCrawler pageCrawler;
	
	@Bean(name="crawlers")
	public List<ICrawler> crawlers(){
		List<ICrawler> crawlers = new ArrayList<ICrawler>();
		
		crawlers.add(pageCrawler);
		//crawlers.add(encadreCrawler);
		
		
		return crawlers;
	}
}
