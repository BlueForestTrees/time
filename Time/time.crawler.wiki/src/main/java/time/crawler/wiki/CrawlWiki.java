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
    private final Chrono lastChrono;
    private final Chrono elapsedChrono;
    private long nbLog;
    private long pageCount;
    private long pageCountPrevision;
    private String urlReplaceFrom = "localhost/mediawiki/index.php";
    private String urlReplaceTo = "fr.wikipedia.org/wiki";

    @Inject
	public CrawlWiki(final Conf conf, final TextHandler store, final TextFactory textFactory) {
		super(conf);
        this.contentExclusion = conf.getContentExclusion();
        this.nbPageLog = conf.getNbPageLog();
        this.lastChrono = new Chrono("Writer");
        this.elapsedChrono = new Chrono("Full");
        this.pageCountPrevision = Optional.ofNullable(this.maxPages).orElse(conf.getPageCountPrevision());
        this.store = store;
        this.textFactory = textFactory;
        LOGGER.info(this);
    }

    @Override
    public void crawl() {
        this.pageCount = 0;
        this.nbLog = 0;
        this.lastChrono.start();
        this.elapsedChrono.start();
        store.start();
        super.crawl();
        store.stop();
    }

    @Override
    public void visit(final Page page) {
        if (notExcludedByContent(page)) {
            final Text text = buildText(page);
            text.getMetadata().setAuteur("Wikipédia");
            store.handleText(text);
            pageCount++;
            logPageProgress();
        }
    }

    private Text buildText(final Page page) {
        final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
        final String url = buildUrl(page.getWebURL().getURL());
        String title = htmlData.getTitle().substring(0, htmlData.getTitle().indexOf(" — "));
        final String textString = htmlData.getText();
        return textFactory.fromString(url, title, textString, Metadata.Type.WIKI);
    }

    private String buildUrl(final String url) {
        return url.replace(urlReplaceFrom, urlReplaceTo);
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
            lastChrono.measure();
            elapsedChrono.measure();
            final String moy = elapsedChrono.toStringDividedBy(nbLog);
            final String remaining = elapsedChrono.getRemaining(pageCount, pageCountPrevision);

            LOGGER.info("{}/{} Texts, Total:{}, Moy:{}, Last:{}, Rest:{}",
                    pageCount, pageCountPrevision, elapsedChrono, moy, lastChrono, remaining);

            lastChrono.start();
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