package wiki;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.domain.Conf;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import java.io.IOException;

public class CrawlWikiModule extends AbstractModule {

	private Conf conf;

	@Provides @Singleton
	public Conf conf() {
		return conf;
	}

	public CrawlWikiModule(final String[] args) throws ArgumentParserException, IOException {
		conf = new ConfManager().get(ConfEnum.WIKICRAWL);
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}

}