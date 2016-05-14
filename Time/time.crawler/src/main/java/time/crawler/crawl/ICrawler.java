package time.crawler.crawl;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public interface ICrawler {
	void start();
    boolean shouldVisit(Page page, WebURL url);
    void visit(Page page);
}
