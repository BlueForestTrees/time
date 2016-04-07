package time.crawler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import time.conf.Conf;
import time.crawler.crawl.CrawlerAdapter;
import time.crawler.crawl.DirectCrawler;
import time.crawler.crawl.IPageHandler;
import time.crawler.write.LogWriter;
import time.crawler.write.LogWriterHelper;

@Component
@ComponentScan({ "time.conf", "time.crawler" })
public class Crawler implements CommandLineRunner {

	private static final Log LOG = LogFactory.getLog(Crawler.class);

	@Autowired
	private Conf conf;

	@Autowired
	private LogWriterHelper logWriterHelper;

	@Bean
	public LogWriter logWriter() {
		final LogWriter writer = new LogWriter();
		writer.setSep(conf.getSep());
		return writer;
	}

	@Bean
	public IPageHandler pageHandler() {
		final DirectCrawler handler = new DirectCrawler();
		handler.setNbPageLog(conf.getNbPageLog());
		handler.setBaseUrl(conf.getBaseUrl());
		handler.setUrlRegexBlackList(conf.getFilter());
		handler.setWriter(logWriter());
		handler.setMaxPages(conf.getMaxPages());
		return handler;
	}

	@Bean
	public edu.uci.ics.crawler4j.crawler.CrawlConfig crawlConfig() {
		edu.uci.ics.crawler4j.crawler.CrawlConfig crawlConfig = new edu.uci.ics.crawler4j.crawler.CrawlConfig();
		crawlConfig.setPolitenessDelay(conf.getDelay());
		crawlConfig.setCrawlStorageFolder(conf.getCrawlStorageFolder());
		crawlConfig.setResumableCrawling(conf.isResumable());
		crawlConfig.setMaxPagesToFetch(conf.getMaxPages());
		return crawlConfig;
	}

	@Bean
	edu.uci.ics.crawler4j.crawler.CrawlController crawlController() throws Exception {
		final RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		final PageFetcher pageFetcher = new PageFetcher(crawlConfig());
		final RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

		edu.uci.ics.crawler4j.crawler.CrawlController crawlController;
		crawlController = new edu.uci.ics.crawler4j.crawler.CrawlController(crawlConfig(), pageFetcher,
				robotstxtServer);
		crawlController.addSeed(conf.getSeedUrl());

		return crawlController;
	}

	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder().bannerMode(Banner.Mode.OFF).sources(Crawler.class).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (conf.isHelp()) {
			LOG.info("configuration du crawler" + conf);
		} else {
			LOG.info("démarrage du crawler" + conf);

			logWriterHelper.configureStorage();
			CrawlerAdapter.pageHandler = pageHandler();
			final CrawlController crawlController = crawlController();
			crawlController.start(CrawlerAdapter.class, conf.getNbCrawlers());

			LOG.info("fin du crawler" + conf);
		}
	}

}
