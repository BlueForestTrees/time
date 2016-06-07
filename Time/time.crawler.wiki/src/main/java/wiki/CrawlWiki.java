package wiki;

import com.google.inject.Inject;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Conf;
import time.crawler.crawl.Crawler;
import time.domain.Text;
import time.storage.store.TextHandler;
import time.tika.TextFactory;
import time.tool.chrono.Chrono;

import java.util.List;

public class CrawlWiki extends Crawler {
	
	private static final Logger LOGGER = LogManager.getLogger(CrawlWiki.class);

    private final List<String> contentExclusion;
    private final TextHandler store;
    private final long nbPageLog;
    private final TextFactory textFactory;
    private final Chrono pageChrono;
    private final Chrono fullChrono;
    private long nbLog;
    private long pageCount;
    private long pageCountPrevision;

    @Inject
	public CrawlWiki(final Conf conf, final TextHandler store, final TextFactory textFactory) {
		super(conf);
        this.contentExclusion = conf.getContentExclusion();
        this.nbPageLog = conf.getNbPageLog();
        this.pageChrono = new Chrono("Writer");
        this.fullChrono = new Chrono("Full");
        this.pageCountPrevision = conf.getPageCountPrevision();
        this.store = store;
        this.textFactory = textFactory;
        LOGGER.info(this);
    }

    @Override
    public void crawl() {
        this.pageCount = 0;
        this.nbLog = 0;
        this.pageChrono.start();
        this.fullChrono.start();
        store.start();
        super.crawl();
        store.stop();
    }

    @Override
    public void visit(final Page page) {
        if (notExcludedByContent(page)) {
            final Text text = buildText(page);
            store.handleText(text);
            pageCount++;
            logPageProgress();
        }
    }

    private Text buildText(final Page page) {
        final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
        final String url = page.getWebURL().getURL();
        final String title = htmlData.getTitle();
        final String textString = htmlData.getText();
        return textFactory.build(url, title, textString);
    }

    private boolean notExcludedByContent(final Page page) {
        final boolean isNotHtml = !(page.getParseData() instanceof HtmlParseData);
        if(isNotHtml){
            return false;
        }
        final String content = ((HtmlParseData) page.getParseData()).getText();
        final boolean excludedByContent =  contentExclusion.stream().noneMatch(content::contains);
        return excludedByContent;
    }

    private void logPageProgress() {
        if (LOGGER.isDebugEnabled() && (pageCount % nbPageLog == 0)) {
            nbLog++;
            pageChrono.tic();
            fullChrono.tic();
            LOGGER.debug(pageCount + "/" + pageCountPrevision + " texts. Total:" + fullChrono + ", Moy:" + fullChrono.toStringDividedBy(nbLog) + ", Last:" + pageChrono + ", Rest:" + fullChrono.getRemaining(pageCount, pageCountPrevision));
            pageChrono.start();
        }
    }

    @Override
    public String toString() {
        return "CrawlWiki{" +
                super.toString() +
                ", contentExclusion=" + contentExclusion +
                ", store=" + store +
                ", nbPageLog=" + nbPageLog +
                ", textFactory=" + textFactory +
                ", nbLog=" + nbLog +
                ", pageCount=" + pageCount +
                ", pageCountPrevision=" + pageCountPrevision +
                '}';
    }
}