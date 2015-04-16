package wiki.crawler;

import edu.uci.ics.crawler4j.crawler.Page;

public interface ICrawler {
	public void visit(Page page);
	public void end();
}
