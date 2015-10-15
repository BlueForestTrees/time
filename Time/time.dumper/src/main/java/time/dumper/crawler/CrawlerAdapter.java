package time.dumper.crawler;

import time.dumper.factory.DumperFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Cette classe est passée à crawl4j.
 * @author Slimane
 *
 */
public class CrawlerAdapter extends WebCrawler {
	private IPageHandler crawler = DumperFactory.getCrawler(DumperFactory.getConfig());
	
	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page page, WebURL url) {
		return crawler.shouldVisit(url.getURL());
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		crawler.visit(page);
	}
	
	@Override
	public void onBeforeExit() {
		crawler.end();
	}
}