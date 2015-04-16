package wiki.crawler;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import wiki.util.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class ConcurrentWriterCrawler extends Thread implements ICrawler {

	public final static ConcurrentLinkedQueue<Page> pages = new ConcurrentLinkedQueue<Page>();
	static final Logger log = Logger.getLogger(ConcurrentWriterCrawler.class);
	static final Logger pagesLogger = Logger.getLogger("pagestore");;


	public final static String PAGE_SEP = "";
	public final static String FIELD_SEP = "";
	public final static String LINK_SEP = "";

	private long pageCount;
	private long nbPageLog;
	private boolean write;
	private boolean end = false;

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public long getNbPageLog() {
		return nbPageLog;
	}

	public void setNbPageLog(long nbPageLog) {
		this.nbPageLog = nbPageLog;
	}
	
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

		while (!end) {
			Page page;
			try {
				page = pages.remove();
				writePage(page);
				pageCount++;
				if (log.isDebugEnabled() && pageCount % nbPageLog == 0) {
					chrono.stop();
					log.debug("page#" + pageCount + ": " + chrono.toString());
					chrono.start();
				}
			} catch (NoSuchElementException e) {
				if (isInterrupted()) {
					log.debug("no such elements and is interrupted, stopping");
					break;
				}
			}
		}
		log.info("arret");
	}

	private void writePage(Page page) {
		if (write && page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlData = ((HtmlParseData) page.getParseData());
			final WebURL webURL = page.getWebURL();
			final String url = webURL.getURL();
			final short depth = webURL.getDepth();
			final String title = htmlData.getTitle();
			final Set<WebURL> liens = htmlData.getOutgoingUrls();
			final Integer nbLiens = liens.size();
			final String text = htmlData.getText();

			pagesLogger.info(url);
			pagesLogger.info(FIELD_SEP);
			pagesLogger.info(String.valueOf(depth));
			pagesLogger.info(FIELD_SEP);
			pagesLogger.info(title);
			pagesLogger.info(FIELD_SEP);
			pagesLogger.info(String.valueOf(nbLiens));
			pagesLogger.info(FIELD_SEP);
			for (WebURL lien : liens) {
				pagesLogger.info(lien.getURL());
				pagesLogger.info(LINK_SEP);
			}
			pagesLogger.info(FIELD_SEP);
			pagesLogger.info(text);
			pagesLogger.info(PAGE_SEP);
		}
	}
	
	public void end() {
		end  = true;
	}

}
