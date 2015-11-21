package time.dumper.controller;

import time.dumper.config.DumperConfig;
import time.dumper.crawler.CrawlerAdapter;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class DumperController {
    
    private static final Logger LOGGER = Logger.getLogger(DumperController.class);
    
    private CrawlController crawlController;
    private int nbCrawlers;
    private DumperConfig config;

    public DumperConfig getConfig() {
        return config;
    }

    public void setConfig(DumperConfig config) {
        this.config = config;
    }

    public int getNbCrawlers() {
        return nbCrawlers;
    }

    public void setNbCrawlers(int nbCrawlers) {
        this.nbCrawlers = nbCrawlers;
    }

    public CrawlController getCrawlController() {
        return crawlController;
    }

    public void setCrawlController(CrawlController crawlController) {
        this.crawlController = crawlController;
    }

    public void start() throws Exception {
        if (config.isHelp()) {
            LOGGER.info("configuration du crawler" + config.getConfAsString());
        } else {
            LOGGER.info("démarrage du crawler" + config.getConfAsString());

            crawlController.start(CrawlerAdapter.class, nbCrawlers);

            LOGGER.info("fin du crawler" + config.getConfAsString());
        }
    }

}
