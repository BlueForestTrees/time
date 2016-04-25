package time.crawler.wiki;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.name.Named;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import time.conf.Conf;
import time.crawler.BaseCrawler;
import time.crawler.write.IWriter;
import time.tool.chrono.Chrono;

public class WikiCrawler extends BaseCrawler {
	
	private static final Logger LOGGER = LogManager.getLogger(WikiCrawler.class);
	        
    protected IWriter writer;
    
    protected Chrono chrono;
    protected Chrono fullChrono;
    	
    private long nbLog;
    private long pageCount;
    private int pageTotal;
    
	public WikiCrawler(@Named("conf") final Conf conf, final IWriter writer) {
		super(conf);
        if (LOGGER.isDebugEnabled()) {
            chrono = new Chrono("Writer");
            chrono.start();
            fullChrono = new Chrono("Full");
            fullChrono.start();
        }
        this.writer = writer;
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            final String content = ((HtmlParseData) page.getParseData()).getText();
            if (conf.getContentExclusion().stream().noneMatch(content::contains)) {               
            	final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
                writer.writePage(page.getWebURL().getURL(), htmlData.getTitle(),null, htmlData.getText());
                pageCount++;
                if (LOGGER.isDebugEnabled() && (pageCount % conf.getNbPageLog() == 0)) {
                    nbLog++;
                    chrono.stop();
                    fullChrono.stop();
                    LOGGER.debug("#" + pageCount + ", Total:" + fullChrono + ", Moy:" + fullChrono.toStringDividedBy(nbLog) + ", last:" + chrono + ", reste:" + fullChrono.getRemaining(pageCount, pageTotal));
                    chrono.start();
                }
            }
        }
    }

}