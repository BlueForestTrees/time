package time.crawler.files;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.storage.transform.ITextTransformer;
import time.storage.transform.NoOpTextTransformer;

import java.io.IOException;

public class FilesModule extends AbstractModule {

	private Conf configuration;

	public FilesModule(final String[] args) throws ArgumentParserException, IOException {
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