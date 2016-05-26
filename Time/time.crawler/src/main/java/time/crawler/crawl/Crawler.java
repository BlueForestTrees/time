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

public abstract class Crawler implements ICrawler {
	private static final Logger LOGGER = LogManager.getLogger(Crawler.class);

	protected final String baseUrl;
	protected final int nbCrawlers;
	protected final int delay;
	protected final String crawlStorageDir;
	protected final boolean resumable;
	protected final int maxPages;
	protected final String seedUrl;

	protected Pattern urlFilterPattern;
    protected Pattern includePattern;
    protected List<String> urlMustNotContain;
	
    public Crawler(@Named("conf") final Conf conf) {
    	this.baseUrl = conf.getBaseUrl();
		this.nbCrawlers = conf.getNbCrawlers();
		this.delay = conf.getPolitenessDelay();
		this.crawlStorageDir = conf.getCrawlStorageDir();
		this.resumable = conf.isResumable();
		this.maxPages = conf.getMaxPages();
		this.seedUrl = conf.getSeedUrl();
        this.urlFilterPattern = conf.getUrlFilter() == null ? null : Pattern.compile(conf.getUrlFilter());
        this.includePattern = conf.getIncludePattern() == null ? null : Pattern.compile(conf.getIncludePattern());
        this.urlMustNotContain = conf.getUrlMustNotContain();
    }
    
	@Override
	public void crawl() {
		LOGGER.info("START WITH CRAWLER : " + this);
		CrawlAdapter.crawler = this;
		final CrawlController crawlController = crawlController();
		crawlController.start(CrawlAdapter.class, nbCrawlers);
	}
	
    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        final String href = url.getURL().toLowerCase();
        final boolean isBaseUrlOk = baseUrl == null || href.startsWith(baseUrl);
        final boolean isUrlFilterExcluded = urlFilterPattern != null && urlFilterPattern.matcher(href).matches();
        final boolean isPatternIncluded = includePattern == null || includePattern.matcher(href).matches();
        final boolean isUrlMustNotContainExcluded = urlMustNotContain != null && urlMustNotContain.stream().anyMatch(href::contains);

        boolean shouldVisit = isBaseUrlOk && !isUrlFilterExcluded && isPatternIncluded && !isUrlMustNotContainExcluded;

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("should visit " + href);
            LOGGER.debug("isBaseUrlOk " + isBaseUrlOk);
            LOGGER.debug("isUrlFilterExcluded " + isUrlFilterExcluded);
            LOGGER.debug("isPatternIncluded " + isPatternIncluded);
            LOGGER.debug("isUrlMustNotContainExcluded " + isUrlMustNotContainExcluded);
            LOGGER.debug("shouldVisit " + shouldVisit);
        }

        return shouldVisit;
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

    @Override
    public String toString() {
        return "Crawler{" +
                "baseUrl='" + baseUrl + '\'' +
                ", nbCrawlers=" + nbCrawlers +
                ", delay=" + delay +
                ", crawlStorageDir='" + crawlStorageDir + '\'' +
                ", resumable=" + resumable +
                ", maxPages=" + maxPages +
                ", seedUrl='" + seedUrl + '\'' +
                ", urlFilterPattern=" + urlFilterPattern +
                ", includePattern=" + includePattern +
                ", urlMustNotContain=" + urlMustNotContain +
                '}';
    }
}
