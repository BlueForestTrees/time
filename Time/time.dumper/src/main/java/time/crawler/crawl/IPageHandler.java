package time.crawler.crawl;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public interface IPageHandler {
    public void visit(Page page);

    public void end();

    public boolean shouldVisit(Page page, WebURL url);
}
