package wiki;

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

    public CrawlWikiMain(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        final CrawlWiki crawlWiki = Guice.createInjector(new CrawlWikiModule(args)).getInstance(CrawlWiki.class);

        final Messager messager = new Messager();
        messager.when(Queue.WIKI_CRAWL).then(() -> {
                    crawlWiki.crawl();
                    try {
                        messager.signal(Queue.WIKI_CRAWL_END);
                        messager.signal(Queue.MERGE);
                    } catch (IOException e) {
                        LOGGER.error(e);
                    }
                });
    }

    public static void main(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        new CrawlWikiMain(args);
    }

}
