package time.crawler.wiki;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.crawler.crawl.BaseCrawler;
import time.repo.bean.Text;
import time.tool.chrono.Chrono;
import time.storage.store.TextStore;

import java.util.List;

public class WikiRun extends BaseCrawler {
	
	private static final Logger LOGGER = LogManager.getLogger(WikiRun.class);

    private final List<String> contentExclusion;
    private final TextStore store;
    private final long nbPageLog;
    private Chrono chrono;
    private Chrono fullChrono;
    private long nbLog;
    private long pageCount;
    private long pageTotal;

    @Inject
	public WikiRun(@Named("conf") final Conf conf, final TextStore store) {
		super(conf);
        this.contentExclusion = conf.getContentExclusion();
        this.nbPageLog = conf.getNbPageLog();
        if (LOGGER.isDebugEnabled()) {
            chrono = new Chrono("Writer");
            chrono.start();
            fullChrono = new Chrono("Full");
            fullChrono.start();
        }
        this.pageTotal = conf.getPageTotal();
        this.store = store;
    }

    @Override
    public void start() {
        super.start();
        store.start();
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            final String content = ((HtmlParseData) page.getParseData()).getText();
            if (contentExclusion.stream().noneMatch(content::contains)) {
            	final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
                final Text text = new Text();
                text.setUrl(page.getWebURL().getURL());
                text.setTitle(htmlData.getTitle());
                text.setText(htmlData.getText());
                store.storeText(text);
                pageCount++;
                if (LOGGER.isDebugEnabled() && (pageCount % nbPageLog == 0)) {
                    nbLog++;
                    chrono.stop();
                    fullChrono.stop();
                    LOGGER.debug("#" + pageCount + ", Total:" + fullChrono + ", Moy:" + fullChrono.toStringDividedBy(nbLog) + ", last:" + chrono + ", reste:" + fullChrono.getRemaining(pageCount, pageTotal));
                    chrono.start();
                }
            }
        }
    }

    @Override
    public void end() {
        store.stop();
    }

}