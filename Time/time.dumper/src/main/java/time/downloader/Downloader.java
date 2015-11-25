package time.downloader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ParamsConfig.Values params;
    @Autowired
    private StorageConfig storageConfig;

    @Bean
    public LogWriter logWriter() {
        final LogWriter writer = new LogWriter();
        writer.setBaseUrl(params.getBaseUrl());
        writer.setSep(params.getSep());
        return writer;
    }

    @Bean
    public IPageHandler pageHandler() {
        final DirectCrawler handler = new DirectCrawler();
        handler.setNbPageLog(params.getNbPageLog());
        handler.setWrite(params.isWrite());
        handler.setBaseUrl(params.getBaseUrl());
        handler.setFilters(params.getFilter());
        handler.setWriter(logWriter());
        handler.setMaxPages(params.getMaxPages());
        return handler;
    }

    @Bean
    public edu.uci.ics.crawler4j.crawler.CrawlConfig crawlConfig() {
        edu.uci.ics.crawler4j.crawler.CrawlConfig crawlConfig = new edu.uci.ics.crawler4j.crawler.CrawlConfig();
        crawlConfig.setPolitenessDelay(params.getDelay());
        crawlConfig.setCrawlStorageFolder(params.getCrawlPath());
        crawlConfig.setResumableCrawling(params.isResumable());
        crawlConfig.setMaxPagesToFetch(params.getMaxPages());
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
        crawlController.addSeed(params.getSeedUrl());

        return crawlController;
    }

    public static void main(String[] args) throws Exception {
       new SpringApplicationBuilder().bannerMode(Banner.Mode.OFF).sources(Downloader.class).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        storageConfig.configureStorage();
        CrawlerAdapter.pageHandler = pageHandler();
        if (params.isHelp()) {
            LOG.info("configuration du crawler" + params.getConfAsString());
        } else {
            LOG.info("démarrage du crawler" + params.getConfAsString());

            final CrawlController crawlController = crawlController();
            crawlController.start(CrawlerAdapter.class, params.getNbCrawlers());

            LOG.info("fin du crawler" + params.getConfAsString());
        }
    }

}
