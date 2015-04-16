package wiki.crawler;

import java.util.regex.Pattern;

import wiki.factory.PageFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class WikiCrawler extends WebCrawler {

	private static final String BASE_URL;
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private ICrawler crawler;
	private PageFactory pageFactory;
	
	static{
		BASE_URL = CrawlerGroup.crawlerGroup.getBaseUrl();
	}

	public WikiCrawler() {
		crawler = CrawlerGroup.crawlerGroup;
		pageFactory = PageFactory.get();
	}

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
		if (page.getParseData() instanceof HtmlParseData) {
			wiki.entity.Page createdPage = pageFactory.buildPage(page);
			crawler.visit(createdPage);
		}
	}
}
