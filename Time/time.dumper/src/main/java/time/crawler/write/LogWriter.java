package time.crawler.write;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class LogWriter implements IWriter {
    private static final String PAGESTORE = "pagestore";
    private static final Logger PAGEWRITER = LogManager.getLogger(PAGESTORE);
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
        final StringBuilder sb = new StringBuilder();
        final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
        final WebURL webURL = page.getWebURL();
        final String url = webURL.getURL();
        final String title = htmlData.getTitle();
        final String text = htmlData.getText();

        sb.append(url);
        sb.append(sep);
        sb.append(title);
        sb.append(sep);
        sb.append(text);
        sb.append(sep);

       	PAGEWRITER.info(sb.toString());
    }

}
