package time.append;

import com.google.inject.*;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import time.conf.Conf;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import java.io.IOException;

public class FileModule extends AbstractModule {

	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(FileModule.class);

	private Conf configuration;

	public FileModule(final String[] args) throws ArgumentParserException, IOException {
		configuration = new ConfManager().get(ConfEnum.APPEND);
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}

	@Provides @Singleton
	public Conf conf() {
		return configuration;
	}


}
