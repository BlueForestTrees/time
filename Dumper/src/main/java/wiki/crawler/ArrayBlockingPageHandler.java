package wiki.crawler;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import wiki.util.Chrono;
import wiki.writer.IWriter;
import edu.uci.ics.crawler4j.crawler.Page;

public class ArrayBlockingPageHandler extends ThreadedHandler {

	
	private final static Logger log = Logger.getLogger(ArrayBlockingPageHandler.class);
	public final static ArrayBlockingQueue<Page> pages = new ArrayBlockingQueue<Page>(100);

	public void visit(Page page) {
		try {
			pages.put(page);
		} catch (InterruptedException e) {
			log.error("ArrayBlockingWriterCrawler:pages.put(page);", e);
		}
	}

	public void run() {
		log.info("demarrage");
		pageCount = 0;
		Chrono chrono = null;
		if (log.isDebugEnabled()) {
			chrono = new Chrono("Writer");
			chrono.start();
		}

		while (!isEnd() || pages.size() > 0) {
			try {
				Page page = pages.take();
				writer.writePage(page);
				pageCount++;
				if (log.isDebugEnabled() && (pageCount % nbPageLog == 0)) {
					chrono.stop();
					log.debug("["+pages.size()+"] page#" + pageCount + ": " + chrono.toString());
					chrono.start();
				}
			} catch (InterruptedException e) {
				log.error("Writer:pages.take();", e);
			}
		}
		if (log.isDebugEnabled()) {
			chrono.stop();
			log.debug("page#" + pageCount + ": " + chrono.toString());
		}
		log.info("arret");
	}
}
