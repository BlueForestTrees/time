package wiki.crawler;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import wiki.factory.DumperFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Cette classe est passée à crawl4j.
 * @author Slimane
 *
 */
public class WikiCrawler extends WebCrawler {
	static final Logger log = Logger.getLogger(WikiCrawler.class);
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	public static String BASE_URL;
	
	private ICrawler crawler = DumperFactory.getCrawler(DumperFactory.getConfig());
	
	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page page, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.startsWith(BASE_URL);
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