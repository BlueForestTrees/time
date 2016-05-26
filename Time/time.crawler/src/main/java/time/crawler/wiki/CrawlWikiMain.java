package time.crawler.wiki;

import com.google.inject.Guice;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CrawlWikiMain {

    private static final Logger LOGGER = LogManager.getLogger(CrawlWikiMain.class);

    private final CrawlWiki crawlWiki;

    public CrawlWikiMain(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        crawlWiki = Guice.createInjector(new CrawlWikiModule(args)).getInstance(CrawlWiki.class);
        final Messager messager = new Messager();
        messager.when(Queue.WIKI_CRAWL).then(() -> {
                    crawlWiki.crawl();
                    try {
                        messager.signal(Queue.WIKI_CRAWL_END);
                    } catch (IOException e) {
                        LOGGER.error(e);
                    }
                })
                .signal(Queue.WIKI_CRAWL);
    }

    public static void main(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        new CrawlWikiMain(args);
    }

}
