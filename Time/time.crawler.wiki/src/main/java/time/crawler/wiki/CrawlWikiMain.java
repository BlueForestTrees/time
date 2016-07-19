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

    private CrawlWikiMain(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        try {
            Guice.createInjector(new CrawlWikiModule(args)).getInstance(CrawlWiki.class).crawl();
            new Messager().signal(Queue.MERGE);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static void main(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        new CrawlWikiMain(args);
    }

}
