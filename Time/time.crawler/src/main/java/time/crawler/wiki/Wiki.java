package time.crawler.wiki;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.crawler.crawl.ICrawler;
import time.crawler.work.write.IWriter;
import time.crawler.work.write.log.LogWriter;

public class Wiki extends AbstractModule {

	private Conf configuration;

	private Wiki(final String[] args) throws ArgumentParserException, IOException {
		configuration = new Args().toBean(args, Conf.class);
	}

	@Override
	protected void configure() {
		bind(ICrawler.class).to(WikiCrawler.class);
		bind(IWriter.class).to(LogWriter.class);
	}
	
	public static void main(final String[] args) throws Exception {
		Guice.createInjector(new Wiki(args)).getInstance(ICrawler.class).start();
	}

	@Provides
	@Named("conf")
	public Conf conf() {
		return configuration;
	}
}
