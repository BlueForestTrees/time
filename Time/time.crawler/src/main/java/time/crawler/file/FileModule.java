package time.crawler.file;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.storage.transform.ITextTransformer;
import time.storage.transform.NoOpTextTransformer;

import java.io.IOException;

public class FileModule extends AbstractModule {

	private Conf configuration;

	public FileModule(final String[] args) throws ArgumentParserException, IOException {
		configuration = new Args().toBean(args, Conf.class);
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(NoOpTextTransformer.class);
	}
	
	public static void main(final String[] args) throws Exception {
		final Injector injector = Guice.createInjector(new FileModule(args));
		final FileRun service = injector.getInstance(FileRun.class);
		service.run();
	}

	@Provides
	@Named("conf")
	public Conf webConfiguration() {
		return configuration;
	}


}
