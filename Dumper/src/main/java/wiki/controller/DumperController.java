package wiki.controller;


import org.apache.log4j.Logger;

import wiki.crawler.CrawlerAdapter;
import wiki.util.Chrono;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class DumperController {
	static final Logger log = Logger.getLogger(DumperController.class);
	
	private CrawlController crawlController;
	private int nbCrawlers;

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

	public void start() throws Exception{
		Chrono chrono = new Chrono("Crawl");
		chrono.start();
		
		crawlController.start(CrawlerAdapter.class, nbCrawlers);
		
		chrono.stop();
		log.info(chrono.toString());
	}
	
}
