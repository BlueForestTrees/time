package time.crawler.web;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Cette classe est passée à crawl4j qui en fait des instances.
 * 
 * @author Slimane
 *
 */
public class CrawlerToSpring extends WebCrawler {
    
    public static ISpringCrawler springBean;
    
    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        return springBean.shouldVisit(page, url);
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        springBean.visit(page);
    }

    @Override
    public void onBeforeExit() {
        springBean.end();
    }
}