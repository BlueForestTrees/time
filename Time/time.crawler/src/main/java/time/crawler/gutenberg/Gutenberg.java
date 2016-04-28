package time.crawler.gutenberg;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.crawler.work.crawl.ICrawler;
import time.crawler.work.write.IWriter;
import time.crawler.work.write.file.FileWriter;

public class Gutenberg extends AbstractModule {

	private Conf configuration;

	private Gutenberg(final String[] args) throws ArgumentParserException, IOException {
		configuration = new Args().toBean(args, Conf.class);
	}

	@Override
	protected void configure() {
		bind(ICrawler.class).to(GutenbergCrawler.class);
		bind(IWriter.class).to(FileWriter.class);
	}
	
	public static void main(final String[] args) throws Exception {
		Guice.createInjector(new Gutenberg(args)).getInstance(ICrawler.class).start();
	}

	@Provides
	@Named("conf")
	public Conf webConfiguration() {
		return configuration;
	}
}
