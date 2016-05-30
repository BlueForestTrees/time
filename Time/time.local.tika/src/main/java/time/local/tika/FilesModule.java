package time.local.tika;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.ConfEnum;
import time.conf.ConfManager;
import time.domain.Analyser;
import time.domain.Conf;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import javax.inject.Singleton;
import java.io.IOException;

public class FilesModule extends AbstractModule {

	private String[] args;

	public FilesModule(final String[] args){
		this.args = args;
	}

	@Provides @Singleton
	public Conf conf() throws IOException, ArgumentParserException {
		return new ConfManager().get(args, ConfEnum.LOCAL_TIKA);
	}

	@Provides @Singleton
	public Analyser analyser() throws IOException {
		return new ConfManager().get(ConfEnum.ANALYSER, Analyser.class);
	}

	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(CompositeTextTransformer.class);
	}
}