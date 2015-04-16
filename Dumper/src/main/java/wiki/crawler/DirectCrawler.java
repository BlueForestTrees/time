package wiki.crawler;

import java.util.Set;

import org.apache.log4j.Logger;

import wiki.util.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class DirectCrawler implements ICrawler {

	private final static Logger pagesLogger = Logger.getLogger("pagestore");
	private final static Logger log = Logger.getLogger(DirectCrawler.class);

	public final static String SEP = "|¨";

	private Chrono chrono;
	private Chrono fullChrono;
	private long pageCount;
	private long nbPageLog;
	private boolean write;
	private long nbLog;
	private String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

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

	public DirectCrawler() {
		if (log.isDebugEnabled()) {
			nbLog = 0;
			chrono = new Chrono("Writer");
			chrono.start();
			fullChrono = new Chrono("Full");
			fullChrono.start();
		}
	}

	public void visit(Page page) {
		if (page.getParseData() instanceof HtmlParseData) {
			if (write) {
				StringBuilder sb = new StringBuilder();

				HtmlParseData htmlData = ((HtmlParseData) page.getParseData());
				final WebURL webURL = page.getWebURL();
				final String url = webURL.getURL();
				final short depth = webURL.getDepth();
				final String title = htmlData.getTitle();
				final Set<WebURL> liens = htmlData.getOutgoingUrls();
				final String text = htmlData.getText();
				int nbLiens = 0;
				StringBuilder sbLiens = new StringBuilder();
				for (WebURL webUrl : liens) {
					String lien = webUrl.getURL();
					if (lien.startsWith(baseUrl)) {
						sbLiens.append(lien);
						sbLiens.append(SEP);
						nbLiens++;
					}
				}

				sb.append(url);
				sb.append(SEP);
				sb.append(String.valueOf(depth));
				sb.append(SEP);
				sb.append(title);
				sb.append(SEP);
				sb.append(String.valueOf(nbLiens));
				sb.append(SEP);
				sb.append(sbLiens.toString());
				sb.append(text);
				sb.append(SEP);

				pagesLogger.info(sb.toString());
			}
			pageCount++;
			if (log.isDebugEnabled() && (pageCount % nbPageLog == 0)) {
				nbLog++;
				chrono.stop();
				fullChrono.stop();
				log.debug("["+fullChrono+"]["+ fullChrono.toStringDividedBy(nbLog) +"/"+nbPageLog+"p] page#" + pageCount + " " + chrono);
				chrono.start();
			}
		}

	}

	public void end() {

	}

}
