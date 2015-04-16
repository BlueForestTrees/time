package wiki.crawler;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import wiki.util.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class ArrayBlockingWriterCrawler extends Thread implements ICrawler {

	private final static Logger pagesLogger = Logger.getLogger("pagestore");
	private final static Logger log = Logger.getLogger(ArrayBlockingWriterCrawler.class);
	public final static ArrayBlockingQueue<Page> pages = new ArrayBlockingQueue<Page>(100);
	public final static String PAGE_SEP = "¨|";
	public final static String FIELD_SEP = "|¨";
	public final static String LINK_SEP = "\n";

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

		while (!end || pages.size() > 0) {
			try {
				writePage(pages.take());
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
			for (WebURL webUrl : liens) {
				String lien = webUrl.getURL();
				if(lien.startsWith("http://fr.wi")){
					pagesLogger.info(lien);
					pagesLogger.info(LINK_SEP);
				}
			}
			pagesLogger.info(FIELD_SEP);
			pagesLogger.info(text);
			pagesLogger.info(PAGE_SEP);
		}
	}

	public void end() {
		end  = true;
		interrupt();
	}

}
