package time.crawler.gutenberg;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.crawler.ICrawler;
import time.crawler.conf.Conf;
import time.crawler.write.IWriter;
import time.crawler.write.log.LogWriter;

public class Gutenberg extends AbstractModule {

	private Conf configuration;

	private Gutenberg(final String[] args) throws ArgumentParserException, IOException {
		configuration = new Args().toBean(args, Conf.class);
	}

	@Override
	protected void configure() {
		bind(ICrawler.class).to(GutenbergCrawler.class);
		bind(IWriter.class).to(LogWriter.class);
	}
	
	public static void main(final String[] args) throws Exception {
		final Injector injector = Guice.createInjector(new Gutenberg(args));
		final ICrawler crawler = injector.getInstance(ICrawler.class);
		crawler.start();
	}

	@Provides
	@Named("conf")
	public Conf webConfiguration() {
		return configuration;
	}
}
