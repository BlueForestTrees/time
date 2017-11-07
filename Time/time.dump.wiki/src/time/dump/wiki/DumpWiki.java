package time.dump.wiki;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Conf;
import time.storage.store.TextHandler;
import time.tika.TextFactory;
import time.tool.chrono.Chrono;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static javax.xml.stream.XMLInputFactory.newInstance;

public class DumpWiki {

    private static final Logger LOGGER = LogManager.getLogger(DumpWiki.class);
    private final String dumpStorageDir;
    private final Integer maxPages;
    private Pattern urlFilterPattern;
    private Pattern includePattern;
    private List<String> urlMustNotContain;
    private final List<String> contentExclusion;
    private final TextHandler store;
    private final long nbPageLog;
    private final TextFactory textFactory;
    private final Chrono lastChrono;
    private final Chrono elapsedChrono;
    private long nbLog;
    private long pageCount;
    private long pageCountPrevision;

    @Inject
    public DumpWiki(final Conf conf, final TextHandler store, final TextFactory textFactory) {
        this.dumpStorageDir = conf.getDumpStorageDir();
        this.maxPages = conf.getMaxPages();
        this.urlFilterPattern = conf.getUrlFilter() == null ? null : Pattern.compile(conf.getUrlFilter());
        this.includePattern = conf.getIncludePattern() == null ? null : Pattern.compile(conf.getIncludePattern());
        this.urlMustNotContain = conf.getUrlMustNotContain();
        this.contentExclusion = conf.getContentExclusion();
        this.nbPageLog = conf.getNbPageLog();
        this.lastChrono = new Chrono("Writer");
        this.elapsedChrono = new Chrono("Full");
        this.pageCountPrevision = Optional.ofNullable(this.maxPages).orElse(conf.getPageCountPrevision());
        this.store = store;
        this.textFactory = textFactory;
        LOGGER.info(this);
    }

    public void crawl() throws FileNotFoundException, XMLStreamException {
        this.pageCount = 0;
        this.nbLog = 0;
        this.lastChrono.start();
        this.elapsedChrono.start();
        store.start();
        dump();
        store.stop();
    }

    private void dump() throws FileNotFoundException, XMLStreamException {
        final XMLEventReader eventReader = newInstance().createXMLEventReader(new FileInputStream(this.dumpStorageDir));
        StringBuilder content = null;
        while (eventReader.hasNext()) {
            final XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                // other start element processing here
                content = new StringBuilder();
                LOGGER.info("<"+event.asStartElement().getName().getLocalPart()+">");
            } else if (event.isEndElement()) {
                if (content != null) {
                    // this was a leaf element
                    String leafText = content.toString();
                    LOGGER.info(leafText);
                } else {
                    // not a leaf
                }
                // in all cases, discard content
                content = null;
            } else if (event.isCharacters()) {
                if (content != null) {
                    content.append(event.asCharacters().getData());
                }
            }
        }
    }


    //    public boolean shouldVisit(Page page, WebURL url) {
//        final String href = url.getURL().toLowerCase();
//        final boolean isBaseUrlOk = baseUrl == null || href.startsWith(baseUrl);
//        final boolean isUrlFilterExcluded = urlFilterPattern != null && urlFilterPattern.matcher(href).matches();
//        final boolean isPatternIncluded = includePattern == null || includePattern.matcher(href).matches();
//        final boolean isUrlMustNotContainExcluded = urlMustNotContain != null && urlMustNotContain.stream().anyMatch(href::contains);
//
//        boolean shouldVisit = isBaseUrlOk && !isUrlFilterExcluded && isPatternIncluded && !isUrlMustNotContainExcluded;
//
//        if(LOGGER.isDebugEnabled()){
//            LOGGER.debug("should visit " + href);
//            LOGGER.debug("isBaseUrlOk " + isBaseUrlOk);
//            LOGGER.debug("isUrlFilterExcluded " + isUrlFilterExcluded);
//            LOGGER.debug("isPatternIncluded " + isPatternIncluded);
//            LOGGER.debug("isUrlMustNotContainExcluded " + isUrlMustNotContainExcluded);
//            LOGGER.debug("shouldVisit " + shouldVisit);
//        }
//
//        return shouldVisit;
//    }

//    public void visit(final Page page) {
//        if (notExcludedByContent(page)) {
//            final Text text = buildText(page);
//            text.getMetadata().setAuteur("Wikipédia");
//            store.handleText(text);
//            pageCount++;
//            logPageProgress();
//        }
//    }
//
//    private Text buildText(final Page page) {
//        final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
//        final String url = page.getWebURL().getURL();
//        final String title = htmlData.getTitle().substring(0, htmlData.getTitle().length()-(" — Wikipédia".length()));
//        final String textString = htmlData.getText();
//        return textFactory.fromString(url, title, textString, Metadata.Type.WIKI);
//    }
//
//    private boolean notExcludedByContent(final Page page) {
//        final boolean isNotHtml = !(page.getParseData() instanceof HtmlParseData);
//        if(isNotHtml){
//            return false;
//        }
//        final String content = ((HtmlParseData) page.getParseData()).getText();
//        final boolean excludedByContent =  contentExclusion.stream().noneMatch(content::contains);
//        return excludedByContent;
//    }

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
        return "time.dump.wiki.DumpWiki{" +
                ", dumpStorageDir='" + dumpStorageDir + '\'' +
                ", maxPages=" + maxPages +
                ", urlFilterPattern=" + urlFilterPattern +
                ", includePattern=" + includePattern +
                ", urlMustNotContain=" + urlMustNotContain +
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