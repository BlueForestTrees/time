package wiki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wiki.crawler.CrawlerGroup;

@RestController
@RequestMapping("/crawl")
public class CrawlController {
	
	@Autowired
	CrawlerGroup crawlerGroup;
	
	@Autowired
	@Qualifier("crawlerCount")
	Integer crawlerCount;
	
	@Autowired
	@Qualifier("crawlController")
	edu.uci.ics.crawler4j.crawler.CrawlController crawlController;
	
	@RequestMapping(method = RequestMethod.GET)
    public String start() throws Exception {

		//crawlController.startNonBlocking(WikiCrawler.class, crawlerCount);	
		
        return "OK!";
    }
	
	
}