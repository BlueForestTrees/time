package time.append;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.logging.log4j.LogManager;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.domain.Analyser;
import time.domain.Conf;
import time.domain.Transformer;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import javax.inject.Singleton;
import java.io.IOException;

public class IndexCreatorModule extends AbstractModule {

	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(IndexCreatorModule.class);

	private Conf conf;

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}

	@Provides @Singleton
	public Conf conf() throws IOException {
		return new ConfManager().get(ConfEnum.APPEND);
	}

	@Provides @Singleton
	public Analyser analyser() throws IOException {
		return new ConfManager().get(ConfEnum.ANALYSER, Analyser.class);
	}

	@Provides @Singleton
	public Transformer transformer() throws IOException {
		return new ConfManager().get(ConfEnum.TRANSFORMER, Transformer.class);
	}


}
