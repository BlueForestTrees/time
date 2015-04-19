package wiki.crawler;

import org.apache.log4j.Logger;

import wiki.util.Chrono;
import wiki.writer.IWriter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class DirectCrawler extends BasePageHandler{

	private final static Logger log = Logger.getLogger(DirectCrawler.class);
	private IWriter writer;

	public IWriter getWriter() {
		return writer;
	}

	public void setWriter(IWriter writer) {
		this.writer = writer;
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
				writer.writePage(page);
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
