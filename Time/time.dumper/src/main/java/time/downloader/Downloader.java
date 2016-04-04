package time.downloader;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import time.downloader.crawler.CrawlerAdapter;
import time.downloader.crawler.DirectCrawler;
import time.downloader.crawler.IPageHandler;
import time.downloader.writer.LogWriter;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Configuration
@ComponentScan({ "time.downloader" })
public class Downloader implements CommandLineRunner{

    private static final Log LOG = LogFactory.getLog(Downloader.class);
    
    @Autowired
    private ApplicationArguments args;
    
    @Autowired
    private Config conf;
    
    @Bean
    public Config configuration() throws IOException{
    	final String basePath = args.getOptionValues(ConfigKeys.basePath).get(0);
		final String name = args.getOptionValues(ConfigKeys.name).get(0);
		final Config config = new Config(basePath, name);
		return config;
    }
    
    @Autowired
    private StorageConfig storageConfig;

    @Bean
    public LogWriter logWriter() {
        final LogWriter writer = new LogWriter();
        writer.setBaseUrl(conf.getBaseUrl());
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
    edu.uci.ics.crawler4j.crawler.CrawlController crawlController() throws DownloaderException {
        final RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        final PageFetcher pageFetcher = new PageFetcher(crawlConfig());
        final RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        edu.uci.ics.crawler4j.crawler.CrawlController crawlController;
        try {
            crawlController = new edu.uci.ics.crawler4j.crawler.CrawlController(crawlConfig(), pageFetcher, robotstxtServer);
        } catch (Exception e) {
            throw new DownloaderException(e);
        }
        crawlController.addSeed(conf.getSeedUrl());

        return crawlController;
    }

    public static void main(String[] args) throws Exception {
       new SpringApplicationBuilder().bannerMode(Banner.Mode.OFF).sources(Downloader.class).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        storageConfig.configureStorage();
        CrawlerAdapter.pageHandler = pageHandler();
        if (conf.isHelp()) {
            LOG.info("configuration du crawler" + conf);
        } else {
            LOG.info("démarrage du crawler" + conf);

            final CrawlController crawlController = crawlController();
            crawlController.start(CrawlerAdapter.class, conf.getNbCrawlers());

            LOG.info("fin du crawler" + conf);
        }
    }

}
