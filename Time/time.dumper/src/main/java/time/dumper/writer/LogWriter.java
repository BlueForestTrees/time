package time.dumper.writer;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class LogWriter implements IWriter {
	private static final Logger LOG = Logger.getLogger("pagestore");
	private String sep;
	private String baseUrl;
	public String getSep() {
		return sep;
	}

	public void setSep(String sep) {
		this.sep = sep;
	}

	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public void writePage(Page page) {
		StringBuilder sb = new StringBuilder();

		HtmlParseData htmlData = (HtmlParseData) page.getParseData();
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
				sbLiens.append(sep);
				nbLiens++;
			}
		}

		sb.append(url);
		sb.append(sep);
		sb.append(String.valueOf(depth));
		sb.append(sep);
		sb.append(title);
		sb.append(sep);
		sb.append(String.valueOf(nbLiens));
		sb.append(sep);
		sb.append(sbLiens.toString());
		sb.append(text);
		sb.append(sep);

		LOG.info(sb.toString());
	}

}
