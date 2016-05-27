package time.local.tika;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Conf;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import java.io.IOException;

public class FilesModule extends AbstractModule {

	private Conf conf;

	public FilesModule(final String[] args) throws ArgumentParserException, IOException {
		conf = new ConfManager().get(args, ConfEnum.LOCAL_TIKA);
	}

	@Provides
	public Conf conf() {
		return conf;
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}
}