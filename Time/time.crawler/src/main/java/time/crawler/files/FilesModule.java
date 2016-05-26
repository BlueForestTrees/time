package time.crawler.files;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Conf;
import time.conf.ConfManager;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import java.io.IOException;

public class FilesModule extends AbstractModule {

	private Conf configuration;

	public FilesModule(final String[] args) throws ArgumentParserException, IOException {
		configuration = new ConfManager().toBean(args, Conf.class, "${TIME_HOME}/conf/local.tika.yml");
	}

	@Provides
	@Named("conf")
	public Conf webConfiguration() {
		return configuration;
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}
}