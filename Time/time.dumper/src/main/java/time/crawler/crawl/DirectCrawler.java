package time.crawler.crawl;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import time.crawler.write.IWriter;
import time.tool.chrono.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class DirectCrawler implements IPageHandler {
    private static final Logger LOGGER = LogManager.getLogger(DirectCrawler.class);
    protected Pattern urlRegexBlackList;
    protected long pageCount;
    protected long nbPageLog;
    protected String baseUrl;
    protected Chrono chrono;
    protected Chrono fullChrono;
    protected long nbLog;
    protected IWriter writer;
    private int pageTotal;
    private String[] urlBlackList = new String[] { "Discussion_utilisateur:","Discussion_utilisatrice:","Utilisatrice:","#", "Discussion_modèle:", "Discussion_mod%C3%A8le:", "Discussion:", "spécial:", "sp%c3%a9cial:", "Discussion_wikipédia:", "discussion_wikipédia:", "discussion_wikip%c3%a9dia:", "cat%c3%a9gorie:", "catégorie:", "utilisateur:", "projet:", "discussion_projet:", "aide:", "wikipédia:", "wikip%c3%a9dia:", "fichier:" };
    private String[] contentExclusion = new String[] { "\t\t\t(Redirigé depuis " };

    public DirectCrawler() {
        if (LOGGER.isDebugEnabled()) {
            nbLog = 0;
            chrono = new Chrono("Writer");
            chrono.start();
            fullChrono = new Chrono("Full");
            fullChrono.start();
        }
    }

    public void setMaxPages(int maxPages) {
        this.pageTotal = maxPages;
    }

    public Pattern getUrlRegexBlackList() {
        return urlRegexBlackList;
    }

    public void setUrlRegexBlackList(Pattern filters) {
        this.urlRegexBlackList = filters;
    }

    public IWriter getWriter() {
        return writer;
    }

    public void setWriter(IWriter writer) {
        this.writer = writer;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getNbPageLog() {
        return nbPageLog;
    }

    public void setNbPageLog(long nbPageLog) {
        this.nbPageLog = nbPageLog;
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            final String content = ((HtmlParseData) page.getParseData()).getText();
            if (Arrays.stream(contentExclusion).noneMatch(term -> content.contains(term))) {
                writer.writePage(page);
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
        // rien
    }

    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        final String href = url.getURL().toLowerCase();
        
        final boolean startsWithBaseUrl = href.startsWith(baseUrl);
        final boolean urlRegexBlackListed = urlRegexBlackList.matcher(href).matches();
        final boolean urlBlackListed = Arrays.stream(urlBlackList).anyMatch(term -> href.contains(term));

        return startsWithBaseUrl && !urlRegexBlackListed && !urlBlackListed;
    }

}