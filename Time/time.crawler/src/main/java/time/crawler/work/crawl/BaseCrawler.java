package time.crawler.work.crawl;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.name.Named;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import time.conf.Conf;

public abstract class BaseCrawler implements ICrawler {
	private static final Logger LOGGER = LogManager.getLogger(BaseCrawler.class);

	protected Conf conf;
    protected Pattern excludePattern;
    protected Pattern includePattern;
    protected List<String> excludeList;
	
    public BaseCrawler(@Named("conf") final Conf conf) {
    	this.conf = conf;
        excludePattern = conf.getExcludePattern() == null ? null : Pattern.compile(conf.getExcludePattern());
        includePattern = conf.getIncludePattern() == null ? null : Pattern.compile(conf.getIncludePattern());
    }
    
	@Override
	public void start() {
		CrawlAdapter.crawler = this;
		final CrawlController crawlController = crawlController();
		crawlController.start(CrawlAdapter.class, conf.getNbCrawlers());
	}
	
    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        final String href = url.getURL().toLowerCase();
        final boolean isBaseUrlOk = conf.getBaseUrl() == null || href.startsWith(conf.getBaseUrl());
        final boolean isPatternExcluded = excludePattern != null && excludePattern.matcher(href).matches();
        final boolean isPatternIncluded = includePattern == null || includePattern.matcher(href).matches();
        final boolean isListExcluded = excludeList != null && excludeList.stream().anyMatch(href::contains);
        return isBaseUrlOk && !isPatternExcluded && isPatternIncluded && !isListExcluded;
    }
    
    @Override
    public void end() {
        // rien
    }
    
    private CrawlConfig crawlConfig() {
		final CrawlConfig crawlConfig = new CrawlConfig();
		crawlConfig.setPolitenessDelay(conf.getDelay());
		crawlConfig.setCrawlStorageFolder(conf.getCrawlStorageDir());
		crawlConfig.setResumableCrawling(conf.isResumable());
		crawlConfig.setMaxPagesToFetch(conf.getMaxPages());
		return crawlConfig;
	}

	private CrawlController crawlController() {
		final RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		final CrawlConfig crawlConfig = crawlConfig();
		final PageFetcher pageFetcher = new PageFetcher(crawlConfig);
		final RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		
		try {
			final CrawlController crawlController = new edu.uci.ics.crawler4j.crawler.CrawlController(crawlConfig, pageFetcher, robotstxtServer);
			crawlController.addSeed(conf.getSeedUrl());
			return crawlController;
		} catch (Exception e) {
			LOGGER.error("crawlerController Construction", e);
			return null;
		}
	}
}
