package wiki.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Configuration
@ComponentScan("wiki.crawler")
public class CrawlConfig {
	
	@Bean
	public Integer crawlerCount() {
		return 1;
	}
	
	@Bean
	public String[] seedUrls() {
		return new String[]{
				"http://fr.wikipedia.org/wiki/Portail:Accueil"
		};
	}
	
	@Bean
	public edu.uci.ics.crawler4j.crawler.CrawlController crawlController() throws Exception {
		String crawlStorageFolder = "/dat/crawl/root";
		edu.uci.ics.crawler4j.crawler.CrawlConfig config = new edu.uci.ics.crawler4j.crawler.CrawlConfig();
		config.setPolitenessDelay(10);
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setResumableCrawling(false);
		config.setMaxPagesToFetch(1000);
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		edu.uci.ics.crawler4j.crawler.CrawlController controller = new edu.uci.ics.crawler4j.crawler.CrawlController(config, pageFetcher, robotstxtServer);
		
		for(String seedUrl : seedUrls()){
			controller.addSeed(seedUrl);
		}

		return controller;
	}

}
