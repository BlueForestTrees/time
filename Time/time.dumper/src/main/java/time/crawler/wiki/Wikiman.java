package time.crawler.wiki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import time.conf.Conf;
import time.crawler.wiki.CrawlerAdapter;
import time.crawler.wiki.DirectCrawler;
import time.crawler.wiki.IPageHandler;
import time.crawler.write.LogWriter;

@Component
public class Wikiman{

	@Autowired
	private Conf conf;
	
	private IPageHandler pageHandler() {
		final DirectCrawler handler = new DirectCrawler();
		handler.setNbPageLog(conf.getNbPageLog());
		handler.setBaseUrl(conf.getBaseUrl());
		handler.setUrlRegexBlackList(conf.getFilter());
		handler.setWriter(new LogWriter());
		handler.setMaxPages(conf.getMaxPages());
		return handler;
	}

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
		CrawlerAdapter.pageHandler = pageHandler();
		final CrawlController crawlController = crawlController();
		crawlController.start(CrawlerAdapter.class, conf.getNbCrawlers());
	}

}
