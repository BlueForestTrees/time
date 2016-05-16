package time.crawler.wiki;

import com.google.inject.Guice;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.messaging.Messager;
import time.messaging.Queues;
import time.messaging.SimpleConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WikiMain  implements SimpleConsumer {

    private final WikiCrawler wikiCrawler;

	public WikiMain(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        wikiCrawler = Guice.createInjector(new WikiModule(args)).getInstance(WikiCrawler.class);
        new Messager().addReceiver(this);
	}

	public static void main(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
		new WikiMain(args);
	}

    @Override
    public String getQueue() {
        return Queues.START_WIKI_CRAWL.name();
    }

    @Override
    public void message() {
        wikiCrawler.start();
    }
}
