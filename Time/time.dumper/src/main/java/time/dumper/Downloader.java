package time.dumper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.dumper.config.DumperConfig;
import time.dumper.controller.DumperController;
import time.dumper.crawler.DirectCrawler;
import time.dumper.crawler.IPageHandler;
import time.dumper.exception.CrawlConstructionException;
import time.dumper.writer.LogWriter;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Configuration
@EnableAutoConfiguration
public class Downloader {

    @Autowired
    private DumperConfig config;
    
    @Bean
    public IPageHandler pageHandler() {
        final DirectCrawler handler = new DirectCrawler();
        handler.setNbPageLog(config.getNbPageLog());
        handler.setWrite(config.isWrite());
        handler.setBaseUrl(config.getBaseUrl());
        handler.setFilters(config.getFilter());
        handler.setWriter(logWriter());
        handler.setMaxPages(config.getMaxPages());
        return handler;
    }
    
    @Bean
    public LogWriter logWriter() {
        final LogWriter writer = new LogWriter();
        writer.setBaseUrl(config.getBaseUrl());
        writer.setSep(config.getSep());
        return writer;
    }
    
    @Bean
    public edu.uci.ics.crawler4j.crawler.CrawlConfig crawlConfig(){
        edu.uci.ics.crawler4j.crawler.CrawlConfig crawlConfig = new edu.uci.ics.crawler4j.crawler.CrawlConfig();
        crawlConfig.setPolitenessDelay(config.getDelay());
        crawlConfig.setCrawlStorageFolder(config.getCrawlPath());
        crawlConfig.setResumableCrawling(config.isResumable());
        crawlConfig.setMaxPagesToFetch(config.getMaxPages());
        return crawlConfig;
    }
    
    @Bean
    edu.uci.ics.crawler4j.crawler.CrawlController crawlController()throws CrawlConstructionException{
        final RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        final PageFetcher pageFetcher = new PageFetcher(crawlConfig());
        final RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        
        edu.uci.ics.crawler4j.crawler.CrawlController crawlController;
        try {
            crawlController = new edu.uci.ics.crawler4j.crawler.CrawlController(crawlConfig(), pageFetcher, robotstxtServer);
        } catch (Exception e) {
            throw new CrawlConstructionException(e);
        }
        crawlController.addSeed(config.getSeedUrl());
        
        return crawlController;
    }
    
    @Bean
    public DumperController dumperController() throws CrawlConstructionException{
        final DumperController dumpController = new DumperController();
        dumpController.setCrawlController(crawlController());
        dumpController.setNbCrawlers(config.getNbCrawlers());
        return dumpController;
    }
        
    public static void main(String[] args) throws Exception {
        final ConfigurableApplicationContext context = SpringApplication.run(Downloader.class, args);
        final DumperController dumpController = context.getBean(DumperController.class);
        dumpController.start();
    }

    
}
