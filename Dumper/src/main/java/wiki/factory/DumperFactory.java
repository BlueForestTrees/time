package wiki.factory;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import wiki.config.DumperConfig;
import wiki.controller.DumperController;
import wiki.crawler.ArrayBlockingPageHandler;
import wiki.crawler.BasePageHandler;
import wiki.crawler.ConcurrentPageHandler;
import wiki.crawler.DirectCrawler;
import wiki.crawler.IPageHandler;
import wiki.crawler.CrawlerAdapter;
import wiki.crawler.ThreadedHandler;
import wiki.writer.IWriter;
import wiki.writer.LogWriter;
import wiki.writer.MongoWriter;
import wiki.writer.layout.PageLayout;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class DumperFactory {

	static final Logger log = Logger.getLogger(DumperFactory.class);

	/**
	 * Juste pour la reference des WikiCrawler. Du grand Mendès
	 */
	private static DumperConfig config;

	public static DumperConfig getConfig() {
		return config;
	}

	public static IPageHandler getCrawler(DumperConfig config) {
		switch (config.getStoreType()) {
		default:
		case ARRAY:
			return getArrayInstance();
		case CONCURRENT:
			return getConcurrentInstance();
		case MONGO:
			return getMongoInstance();
		case DIRECT:
			return getDirectInstance();
		}
	}

	private static DirectCrawler directHandler;
	private static IPageHandler getMongoInstance() {
		if (directHandler == null) {
			directHandler = new DirectCrawler();
			directHandler.setNbPageLog(config.getNbPageLog());
			directHandler.setWrite(config.isWrite());
			directHandler.setBaseUrl(config.getBaseUrl());
			directHandler.setFilters(config.getFilter());
			directHandler.setWriter(getMongoWriter());
		}
		return directHandler;
	}
	private static IWriter getMongoWriter() {
		MongoWriter mongoWriter = new MongoWriter();
		return mongoWriter;
	}

	private static IPageHandler getDirectInstance() {
		if (directHandler == null) {
			directHandler = new DirectCrawler();
			directHandler.setNbPageLog(config.getNbPageLog());
			directHandler.setWrite(config.isWrite());
			directHandler.setBaseUrl(config.getBaseUrl());
			directHandler.setFilters(config.getFilter());
			directHandler.setWriter(getLogWriter());
		}
		return directHandler;
	}

	private static ConcurrentPageHandler concurrentHandler;

	private static IPageHandler getConcurrentInstance() {
		if (concurrentHandler == null) {
			concurrentHandler = new ConcurrentPageHandler();
			concurrentHandler.setNbPageLog(config.getNbPageLog());
			concurrentHandler.setWrite(config.isWrite());
			concurrentHandler.setFilters(config.getFilter());
			concurrentHandler.setWriter(getLogWriter());
			concurrentHandler.start();
		}
		return concurrentHandler;
	}

	public static LogWriter getLogWriter() {
		LogWriter writer = new LogWriter();
		writer.setBaseUrl(config.getBaseUrl());
		writer.setSep(config.getSep());
		return writer;
	}

	private static ThreadedHandler arrayHandler;

	private static IPageHandler getArrayInstance() {
		if (arrayHandler == null) {
			arrayHandler = new ArrayBlockingPageHandler();
			arrayHandler.setNbPageLog(config.getNbPageLog());
			arrayHandler.setWrite(config.isWrite());
			arrayHandler.setFilters(config.getFilter());
			arrayHandler.setWriter(getLogWriter());
			arrayHandler.start();
		}
		return arrayHandler;
	}

	public static final String HISTOIRE_ARG_PREFIX = "-H";
	private static final String SEED_URL = "seedUrl";
	private static final String NB_CRAWLERS = "nbCrawlers";
	private static final String MAX_PAGES = "maxPages";
	private static final String DELAY = "delay";
	private static final String CRAWL_PATH = "crawlPath";
	private static final String BASE_URL = "baseUrl";
	private static final String STORE_TYPE = "storeType";
	private static final String NB_PAGE_LOG = "nbPageLog";
	private static final String RESUMABLE = "resumable";
	private static final String WRITE = "write";
	private static final String HELP = "help";
	private static final String STORAGE_PATH = "storagePath";
	private static final String MAX_FILE_SIZE = "maxFileSize";

	public DumperConfig fromArguments(String[] args) throws Exception {

		DumperConfig config = new DumperConfig();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith(HISTOIRE_ARG_PREFIX)) {
				String parameter = arg.replace(HISTOIRE_ARG_PREFIX, "");
				i++;
				String value = args[i];
				setParameter(config, parameter, value);
			} else {
				log.warn("unknown parameter: " + arg);
			}
		}
		return config;
	}

	private void setParameter(DumperConfig config, final String parameter, final String value) {
		if (parameter.toLowerCase().equals(SEED_URL.toLowerCase())) {
			config.setSeedUrl(value);
		} else if (parameter.toLowerCase().equals(NB_CRAWLERS.toLowerCase())) {
			config.setNbCrawlers(Integer.parseInt(value));
		} else if (parameter.toLowerCase().equals(DELAY.toLowerCase())) {
			config.setDelay(Integer.parseInt(value));
		} else if (parameter.toLowerCase().equals(MAX_PAGES.toLowerCase())) {
			config.setMaxPages(Integer.parseInt(value));
		} else if (parameter.toLowerCase().equals(CRAWL_PATH.toLowerCase())) {
			config.setCrawlPath(value);
		} else if (parameter.toLowerCase().equals(BASE_URL.toLowerCase())) {
			config.setBaseUrl(value);
		} else if (parameter.toLowerCase().equals(STORE_TYPE.toLowerCase())) {
			config.setStoreType(DumperConfig.StoreType.fromString(value));
		} else if (parameter.toLowerCase().equals(NB_PAGE_LOG.toLowerCase())) {
			config.setNbPageLog(Integer.parseInt(value));
		} else if (parameter.toLowerCase().equals(RESUMABLE.toLowerCase())) {
			config.setResumable(Boolean.parseBoolean(value));
		} else if (parameter.toLowerCase().equals(WRITE.toLowerCase())) {
			config.setWrite(Boolean.parseBoolean(value));
		} else if (parameter.toLowerCase().equals(HELP.toLowerCase())) {
			config.setHelp(Boolean.parseBoolean(value));
		} else if (parameter.toLowerCase().equals(STORAGE_PATH.toLowerCase())) {
			config.setStoragePath(value);
		} else if (parameter.toLowerCase().equals(MAX_FILE_SIZE.toLowerCase())) {
			config.setMaxFileSize(value);
		} else {
			log.warn("unknown parameter: " + parameter + "=" + value);
		}
	}

	public DumperController buildController(DumperConfig config) throws Exception {
		String seedUrl = config.getSeedUrl();
		Integer politenessDelay = config.getDelay();
		Integer maxPagesToFetch = config.getMaxPages();
		String crawlStorageFolder = config.getCrawlPath();
		String baseUrl = config.getBaseUrl();
		boolean resumable = config.isResumable();
		int nbCrawler = config.getNbCrawlers();
		String storagePath = config.getStoragePath();
		String maxFileSize = config.getMaxFileSize();

		edu.uci.ics.crawler4j.crawler.CrawlConfig crawlConfig = new edu.uci.ics.crawler4j.crawler.CrawlConfig();
		crawlConfig.setPolitenessDelay(politenessDelay);
		crawlConfig.setCrawlStorageFolder(crawlStorageFolder);
		crawlConfig.setResumableCrawling(resumable);
		crawlConfig.setMaxPagesToFetch(maxPagesToFetch);
		PageFetcher pageFetcher = new PageFetcher(crawlConfig);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		edu.uci.ics.crawler4j.crawler.CrawlController controller = new edu.uci.ics.crawler4j.crawler.CrawlController(crawlConfig, pageFetcher, robotstxtServer);
		controller.addSeed(seedUrl);

		configureLogs(storagePath, maxFileSize);
		DumperFactory.config = config;

		DumperController dumpController = new DumperController();
		dumpController.setCrawlController(controller);
		dumpController.setNbCrawlers(nbCrawler);
		return dumpController;
	}

	private void configureLogs(String storagePath, String maxFileSize) {
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		String fileName = storagePath;
		String filePattern = storagePath + "%i";
		String append = "false";
		String name = "pagestore";
		String immediateFlush = "true";
		String bufferSizeStr = String.valueOf(RollingRandomAccessFileManager.DEFAULT_BUFFER_SIZE);
		TriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(maxFileSize);
		RolloverStrategy strategy = DefaultRolloverStrategy.createStrategy("1000", null, null, null, config);
		Layout<? extends Serializable> layout = new PageLayout();
			

		RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.createAppender(fileName, filePattern, append, name, immediateFlush,
				bufferSizeStr, policy, strategy, layout, null, "true", "true", null, config);
		
		appender.start();
		config.addAppender(appender);

		AppenderRef ref = AppenderRef.createAppenderRef("pagestore", null, null);
		AppenderRef[] refs = new AppenderRef[] { ref };
		LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.INFO, "pagestore", "true", refs, null, config, null);
		loggerConfig.addAppender(appender, null, null);
		config.addLogger("pagestore", loggerConfig);
		ctx.updateLoggers();

	}

}
