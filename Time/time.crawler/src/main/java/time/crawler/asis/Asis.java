package time.crawler.asis;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.crawler.work.write.IWriter;
import time.crawler.work.write.file.FileWriter;

public class Asis extends AbstractModule {

	private Conf configuration;

	private Asis(final String[] args) throws ArgumentParserException, IOException {
		configuration = new Args().toBean(args, Conf.class);
	}

	@Override
	protected void configure() {
		bind(IWriter.class).to(FileWriter.class);
	}
	
	public static void main(final String[] args) throws Exception {
		final Injector injector = Guice.createInjector(new Asis(args));
		final AsisService service = injector.getInstance(AsisService.class);
		service.run();
	}

	@Provides
	@Named("conf")
	public Conf webConfiguration() {
		return configuration;
	}


}
