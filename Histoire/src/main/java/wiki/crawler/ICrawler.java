package wiki.crawler;

import wiki.entity.Page;


public interface ICrawler {
	public void visit(Page page);
}
