package wiki.crawler;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import wiki.util.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;

public class ConcurrentPageHandler extends ThreadedHandler {

	public final static ConcurrentLinkedQueue<Page> pages = new ConcurrentLinkedQueue<Page>();
	static final Logger log = Logger.getLogger(ConcurrentPageHandler.class);
	
	public void visit(Page page) {
		pages.add(page);
	}

	public void run() {
		log.info("demarrage");
		pageCount = 0;
		Chrono chrono = null;
		if (log.isDebugEnabled()) {
			chrono = new Chrono("Writer");
			chrono.start();
		}

		while (!isEnd()) {
			Page page;
			try {
				page = pages.remove();
				writer.writePage(page);
				pageCount++;
				if (log.isDebugEnabled() && pageCount % nbPageLog == 0) {
					chrono.stop();
					log.debug("page#" + pageCount + ": " + chrono.toString());
					chrono.start();
				}
			} catch (NoSuchElementException e) {
				if (isEnd()) {
					log.debug("no such elements and is interrupted, stopping");
					break;
				}
			}
		}
		log.info("arret");
	}

}
