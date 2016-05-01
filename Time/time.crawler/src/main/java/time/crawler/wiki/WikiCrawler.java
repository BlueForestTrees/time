package time.crawler.wiki;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.name.Named;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import time.conf.Conf;
import time.crawler.work.crawl.BaseCrawler;
import time.crawler.work.write.IWriter;
import time.repo.bean.Text;
import time.tool.chrono.Chrono;

public class WikiCrawler extends BaseCrawler {
	
	private static final Logger LOGGER = LogManager.getLogger(WikiCrawler.class);
	        
    protected IWriter writer;
    
    protected Chrono chrono;
    protected Chrono fullChrono;
    	
    private long nbLog;
    private long pageCount;
    private int pageTotal;

    @Inject
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
                Text mpage = new Text();
                mpage.setUrl(page.getWebURL().getURL());
                mpage.setTitle(htmlData.getTitle());
                mpage.setText(htmlData.getText());
                writer.writePage(mpage);
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