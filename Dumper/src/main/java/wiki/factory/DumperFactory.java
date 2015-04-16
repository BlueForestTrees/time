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
import wiki.crawler.ArrayBlockingWriterCrawler;
import wiki.crawler.ConcurrentWriterCrawler;
import wiki.crawler.DirectCrawler;
import wiki.crawler.ICrawler;
import wiki.crawler.MongoCrawler;
import wiki.crawler.WikiCrawler;
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

	public static ICrawler getCrawler(DumperConfig config) {
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

	private static DirectCrawler directCrawler;
	private static ICrawler getDirectInstance() {
		if (directCrawler == null) {
			directCrawler = new DirectCrawler();
			directCrawler.setNbPageLog(config.getNbPageLog());
			directCrawler.setWrite(config.isWrite());
			directCrawler.setBaseUrl(config.getBaseUrl());
		}
		return directCrawler;
	}

	private static MongoCrawler mongoCrawler;

	private static ICrawler getMongoInstance() {
		if (mongoCrawler == null) {
			mongoCrawler = new MongoCrawler();
			mongoCrawler.setNbPageLog(config.getNbPageLog());
			mongoCrawler.setWrite(config.isWrite());
		}
		return mongoCrawler;
	}

	private static ConcurrentWriterCrawler concurrentCrawler;

	private static ICrawler getConcurrentInstance() {
		if (concurrentCrawler == null) {
			concurrentCrawler = new ConcurrentWriterCrawler();
			concurrentCrawler.setNbPageLog(config.getNbPageLog());
			concurrentCrawler.setWrite(config.isWrite());
			concurrentCrawler.start();
		}
		return concurrentCrawler;
	}

	private static ArrayBlockingWriterCrawler arrayCrawler;

	private static ICrawler getArrayInstance() {
		if (arrayCrawler == null) {
			arrayCrawler = new ArrayBlockingWriterCrawler();
			arrayCrawler.setNbPageLog(config.getNbPageLog());
			arrayCrawler.setWrite(config.isWrite());
			arrayCrawler.start();
		}
		return arrayCrawler;
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

		WikiCrawler.BASE_URL = baseUrl;
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
