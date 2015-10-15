package time.dumper.crawler;

import edu.uci.ics.crawler4j.crawler.Page;

public interface IPageHandler {
	public void visit(Page page);
	public void end();
	public boolean shouldVisit(String url);
}
