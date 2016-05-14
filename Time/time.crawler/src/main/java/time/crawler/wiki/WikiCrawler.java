package time.crawler.wiki;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.crawler.crawl.Crawler;
import time.storage.store.TextStore;
import time.tika.TextFactory;
import time.tool.chrono.Chrono;

import java.util.List;

public class WikiCrawler extends Crawler {
	
	private static final Logger LOGGER = LogManager.getLogger(WikiCrawler.class);

    private final List<String> contentExclusion;
    private final TextStore store;
    private final long nbPageLog;
    private final TextFactory textFactory;
    private Chrono chrono;
    private Chrono fullChrono;
    private long nbLog;
    private long pageCount;
    private long pageTotal;

    @Inject
	public WikiCrawler(@Named("conf") final Conf conf, final TextStore store, final TextFactory textFactory) {
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
        this.textFactory = textFactory;
        LOGGER.info(this);
    }

    @Override
    public void start() {
        store.start();
        super.start();
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            final String content = ((HtmlParseData) page.getParseData()).getText();
            if (contentExclusion.stream().noneMatch(content::contains)) {
            	final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
                store.storeText(textFactory.build(page.getWebURL().getURL(), htmlData.getTitle(), htmlData.getText()));
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

    @Override
    public String toString() {
        return "WikiCrawler{" +
                "contentExclusion=" + contentExclusion +
                ", store=" + store +
                ", nbPageLog=" + nbPageLog +
                ", textFactory=" + textFactory +
                ", nbLog=" + nbLog +
                ", pageCount=" + pageCount +
                ", pageTotal=" + pageTotal +
                '}';
    }
}