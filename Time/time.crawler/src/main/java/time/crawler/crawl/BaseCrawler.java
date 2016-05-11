package time.crawler.crawl;

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

	protected final String baseUrl;
	protected final int nbCrawlers;
	protected final int delay;
	protected final String crawlStorageDir;
	protected final boolean resumable;
	protected final int maxPages;
	protected final String seedUrl;

	protected Pattern excludePattern;
    protected Pattern includePattern;
    protected List<String> excludeList;
	
    public BaseCrawler(@Named("conf") final Conf conf) {
    	this.baseUrl = conf.getBaseUrl();
		this.nbCrawlers = conf.getNbCrawlers();
		this.delay = conf.getDelay();
		this.crawlStorageDir = conf.getCrawlStorageDir();
		this.resumable = conf.isResumable();
		this.maxPages = conf.getMaxPages();
		this.seedUrl = conf.getSeedUrl();

        excludePattern = conf.getExcludePattern() == null ? null : Pattern.compile(conf.getExcludePattern());
        includePattern = conf.getIncludePattern() == null ? null : Pattern.compile(conf.getIncludePattern());
    }
    
	@Override
	public void start() {
		CrawlAdapter.crawler = this;
		final CrawlController crawlController = crawlController();
		crawlController.start(CrawlAdapter.class, nbCrawlers);
	}
	
    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        final String href = url.getURL().toLowerCase();
        final boolean isBaseUrlOk = baseUrl == null || href.startsWith(baseUrl);
        final boolean isPatternExcluded = excludePattern != null && excludePattern.matcher(href).matches();
        final boolean isPatternIncluded = includePattern == null || includePattern.matcher(href).matches();
        final boolean isListExcluded = excludeList != null && excludeList.stream().anyMatch(href::contains);
        return isBaseUrlOk && !isPatternExcluded && isPatternIncluded && !isListExcluded;
    }
    
    private CrawlConfig crawlConfig() {
		if(crawlStorageDir == null){
			throw new RuntimeException("crawlStorageDir n'est pas défini!");
		}
		final CrawlConfig crawlConfig = new CrawlConfig();
		crawlConfig.setPolitenessDelay(delay);
		crawlConfig.setCrawlStorageFolder(crawlStorageDir);
		crawlConfig.setResumableCrawling(resumable);
		crawlConfig.setMaxPagesToFetch(maxPages);
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
			crawlController.addSeed(seedUrl);
			return crawlController;
		} catch (Exception e) {
			LOGGER.error("crawlerController Construction", e);
			return null;
		}
	}
}
