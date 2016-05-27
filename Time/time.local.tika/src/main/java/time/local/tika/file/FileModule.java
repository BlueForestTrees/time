package time.local.tika.file;

import com.google.inject.*;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import time.conf.Conf;
import time.conf.ConfManager;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import java.io.IOException;

public class FileModule extends AbstractModule {

	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(FileModule.class);

	private Conf configuration;

	public FileModule(final String[] args) throws ArgumentParserException, IOException {
		if(args.length == 0){
			LOGGER.error("manque: --conf=${TIME_HOME}/conf/fileconf.yml");
		}
		configuration = new ConfManager().get(args);
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
