package time.dumper.crawler;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import time.dumper.writer.IWriter;
import time.tool.chrono.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class DirectCrawler implements IPageHandler {
    private static final Logger LOGGER = Logger.getLogger(DirectCrawler.class);
    protected Pattern filters;
    protected long pageCount;
    protected long nbPageLog;
    protected boolean write;
    protected String baseUrl;
    protected Chrono chrono;
    protected Chrono fullChrono;
    protected long nbLog;
    protected IWriter writer;
    private int pageTotal;
    private String[] toExclude = new String[] { "spécial:", "sp%c3%a9cial:", "discussion_wikipédia:", "discussion_wikip%c3%a9dia:", "cat%c3%a9gorie:", "catégorie:", "utilisateur:", "projet:", "discussion_projet:", "aide:", "wikipédia:", "wikip%c3%a9dia:", "fichier:" };

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

    public Pattern getFilters() {
        return filters;
    }

    public void setFilters(Pattern filters) {
        this.filters = filters;
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

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public long getNbPageLog() {
        return nbPageLog;
    }

    public void setNbPageLog(long nbPageLog) {
        this.nbPageLog = nbPageLog;
    }

    @Override
    public boolean shouldVisit(String url) {
        final String href = url.toLowerCase();
        if (href.startsWith(baseUrl) && !filters.matcher(href).matches() && !Arrays.stream(toExclude).anyMatch(term -> href.contains(term))) {
            return true;
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            if (write) {
                writer.writePage(page);
            }
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

    @Override
    public void end() {
        // rien
    }

}