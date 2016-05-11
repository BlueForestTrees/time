package time.crawler.wiki;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.storage.transform.ITextTransformer;
import time.storage.transform.WikiExcludeAfterTextTransformer;

import java.io.IOException;

public class WikiModule extends AbstractModule {

	private Conf conf;

	@Provides
	@Named("conf")
	public Conf conf() {
		return conf;
	}

	public WikiModule(final String[] args) throws ArgumentParserException, IOException {
		conf = new Args().toBean(args, Conf.class);
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(WikiExcludeAfterTextTransformer.class);
	}

}
