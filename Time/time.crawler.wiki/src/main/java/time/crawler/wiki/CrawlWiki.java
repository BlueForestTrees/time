package time.crawler.wiki;

import com.google.inject.Inject;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Conf;
import time.crawler.crawl.Crawler;
import time.domain.Metadata;
import time.domain.Text;
import time.storage.store.TextHandler;
import time.tika.TextFactory;
import time.tool.chrono.Chrono;

import java.util.List;
import java.util.Optional;

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
        this.pageCountPrevision = Optional.ofNullable(this.maxPages).orElse(conf.getPageCountPrevision());
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
        final String title = htmlData.getTitle().substring(0, htmlData.getTitle().length()-(" — Wikipédia".length()));
        final String textString = htmlData.getText();
        return textFactory.fromString(url, title, textString, Metadata.Type.WIKI);
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
        if (LOGGER.isInfoEnabled() && (pageCount % nbPageLog == 0)) {
            nbLog++;
            pageChrono.tic();
            fullChrono.tic();
            LOGGER.info(pageCount + "/" + pageCountPrevision + " texts. Total:" + fullChrono + ", Moy:" + fullChrono.toStringDividedBy(nbLog) + ", Last:" + pageChrono + ", Rest:" + fullChrono.getRemaining(pageCount, pageCountPrevision));
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