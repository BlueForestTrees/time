package time.transformer;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.transformer.page.transformer.IPageTransformer;
import time.transformer.page.transformer.NoOpPageTransformer;

public class Transformer extends AbstractModule{
    
	private Conf conf;

	private Transformer(final String[] args) throws ArgumentParserException, IOException {
		conf = new Args().toBean(args, Conf.class);
	}

	@Override
	protected void configure() {
		bind(IPageTransformer.class).to(NoOpPageTransformer.class);
	}
	
	public static void main(final String[] args) throws Exception {
		final Injector injector = Guice.createInjector(new Transformer(args));
		injector.getInstance(Runner.class).run();
	}

	@Provides
	@Named("conf")
	public Conf conf() {
		return conf;
	}

}
