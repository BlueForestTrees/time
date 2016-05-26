package time.crawler.wiki;

import com.google.inject.Guice;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;
import time.messaging.Queue;
import time.messaging.SimpleConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CrawlWikiMain implements SimpleConsumer {

    private static final Logger LOGGER = LogManager.getLogger(CrawlWikiMain.class);

    private final CrawlWiki crawlWiki;

	public CrawlWikiMain(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        crawlWiki = Guice.createInjector(new CrawlWikiModule(args)).getInstance(CrawlWiki.class);
        new Messager().addReceiver(this).getSender(Queue.START_WIKI_CRAWL).signal();
	}

	public static void main(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
		new CrawlWikiMain(args);
	}

    @Override
    public Queue getQueue() {
        return Queue.START_WIKI_CRAWL;
    }

    @Override
    public void message() {
        crawlWiki.start();
        try {
            new Messager().getSender(Queue.START_WIKI_CRAWL_END).signal();
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
        }
    }
}
