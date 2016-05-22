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

public class WikiMain  implements SimpleConsumer {

    private static final Logger LOGGER = LogManager.getLogger(WikiMain.class);

    private final WikiCrawler wikiCrawler;

	public WikiMain(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        wikiCrawler = Guice.createInjector(new WikiModule(args)).getInstance(WikiCrawler.class);
        new Messager().addReceiver(this).getSender(Queue.START_WIKI_CRAWL).signal();
	}

	public static void main(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
		new WikiMain(args);
	}

    @Override
    public Queue getQueue() {
        return Queue.START_WIKI_CRAWL;
    }

    @Override
    public void message() {
        wikiCrawler.start();
        try {
            new Messager().getSender(Queue.MERGE).signal();
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
        }
    }
}
