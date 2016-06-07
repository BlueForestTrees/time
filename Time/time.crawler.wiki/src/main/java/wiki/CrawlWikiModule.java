package wiki;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.domain.Analyser;
import time.domain.Conf;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.domain.Transformer;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import javax.inject.Singleton;
import java.io.IOException;

public class CrawlWikiModule extends AbstractModule {

	private Conf conf;

	@Provides @Singleton
	public Conf conf() {
		return conf;
	}

	@Provides @Singleton
	public Transformer transformer() throws IOException {
		return new ConfManager().get(ConfEnum.TRANSFORMER, Transformer.class);
	}

    @Provides @Singleton
    public Analyser analyser() throws IOException {
        return new ConfManager().get(ConfEnum.ANALYSER, Analyser.class);
    }

	public CrawlWikiModule(final String[] args) throws ArgumentParserException, IOException {
		conf = new ConfManager().get(ConfEnum.WIKICRAWL);
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}

}