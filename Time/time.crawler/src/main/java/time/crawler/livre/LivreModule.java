package time.crawler.livre;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.transformer.transform.ITextTransformer;
import time.transformer.transform.NoOpTextTransformer;

import java.io.IOException;

public class LivreModule extends AbstractModule {

	private Conf configuration;

	public LivreModule(final String[] args) throws ArgumentParserException, IOException {
		configuration = new Args().toBean(args, Conf.class);
	}

	@Provides
	@Named("conf")
	public Conf webConfiguration() {
		return configuration;
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(NoOpTextTransformer.class);
	}
}