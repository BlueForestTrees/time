package time.crawler.web;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.conf.Conf;
import time.crawler.write.IWriter;
import time.tool.chrono.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

@Component
public class SpringCrawler implements ISpringCrawler {
	
    private static final Logger LOGGER = LogManager.getLogger(SpringCrawler.class);
    
    @Autowired
    protected Conf conf;
    
    @Autowired
    protected IWriter writer;

    //TODO faire marcher tout ceci
    protected Pattern urlRegexBlackList;
    protected Chrono chrono;
    protected Chrono fullChrono;
    private long nbLog;
    private long pageCount;
    private int pageTotal;
    
	public SpringCrawler() {
        if (LOGGER.isDebugEnabled()) {
            nbLog = 0;
            chrono = new Chrono("Writer");
            chrono.start();
            fullChrono = new Chrono("Full");
            fullChrono.start();
        }
    }
	
    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            final String content = ((HtmlParseData) page.getParseData()).getText();
            if (Arrays.stream(conf.getContentExclusion()).noneMatch(term -> content.contains(term))) {               
            	final HtmlParseData htmlData = (HtmlParseData) page.getParseData();
                writer.writePage(page.getWebURL().getURL(), htmlData.getTitle(), htmlData.getText());
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

    @Override
    public void end() {
        // rien
    }

    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        final String href = url.getURL().toLowerCase();
        final boolean startsWithBaseUrl = href.startsWith(conf.getBaseUrl());
        final boolean urlRegexBlackListed = urlRegexBlackList.matcher(href).matches();
        final boolean urlBlackListed = Arrays.stream(conf.getUrlBlackList()).anyMatch(term -> href.contains(term));

        return startsWithBaseUrl && !urlRegexBlackListed && !urlBlackListed;
    }

}