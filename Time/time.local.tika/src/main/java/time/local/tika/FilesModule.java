package time.local.tika;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.domain.Analyser;
import time.domain.Conf;
import time.domain.Transformer;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import javax.inject.Singleton;
import java.io.IOException;

class FilesModule extends AbstractModule {

	private String[] args;

	FilesModule(final String[] args){
		this.args = args;
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}

	@Provides @Singleton
	public Conf conf() throws IOException, ArgumentParserException {
		return new ConfManager().get(args, ConfEnum.LOCAL_TIKA);
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