package time.crawler.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import time.conf.Conf;

@Component
public class Webman{

	@Autowired
	private Conf conf;
	
	@Autowired
	private ISpringCrawler springCrawler;

	private CrawlConfig crawlConfig() {
		final CrawlConfig crawlConfig = new CrawlConfig();
		crawlConfig.setPolitenessDelay(conf.getDelay());
		crawlConfig.setCrawlStorageFolder(conf.getCrawlStorageDir());
		crawlConfig.setResumableCrawling(conf.isResumable());
		crawlConfig.setMaxPagesToFetch(conf.getMaxPages());
		return crawlConfig;
	}

	private CrawlController crawlController() throws Exception {
		final RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		final CrawlConfig crawlConfig = crawlConfig();
		final PageFetcher pageFetcher = new PageFetcher(crawlConfig);
		final RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		final CrawlController crawlController = new edu.uci.ics.crawler4j.crawler.CrawlController(crawlConfig, pageFetcher, robotstxtServer);
		crawlController.addSeed(conf.getSeedUrl());

		return crawlController;
	}

	public void go() throws Exception {
		CrawlerToSpring.springBean = springCrawler;
		final CrawlController crawlController = crawlController();
		crawlController.start(CrawlerToSpring.class, conf.getNbCrawlers());
	}

}
