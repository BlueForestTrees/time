package time.crawler.gutenberg;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import time.crawler.BaseCrawler;
import time.crawler.conf.Conf;
import time.crawler.write.IWriter;

public class GutenbergCrawler extends BaseCrawler {
	private static final Logger LOGGER = LogManager.getLogger(GutenbergCrawler.class);
	        
    protected IWriter writer;
    	
    @Inject
	public GutenbergCrawler(@Named("conf") final Conf conf, final IWriter writer) {
		super(conf);
		this.writer = writer;
    }

    @Override
    public void visit(Page page) {
        LOGGER.info(page.getWebURL().getURL());
        if (page.getParseData() instanceof HtmlParseData) {
            final Set<WebURL> outgoingUrls = ((HtmlParseData) page.getParseData()).getOutgoingUrls();
            outgoingUrls.stream().filter(url -> url.getURL().endsWith(".epub")).forEach(url -> writer.writePage(url.getURL(), null, null, null));
        }
    }

}