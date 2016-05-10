package time.transformer.read2store;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.analyser.find.DatedPhrasesFinders;
import time.conf.Args;
import time.conf.Conf;
import time.transformer.transform.ITextTransformer;
import time.transformer.transform.NoOpTextTransformer;

public class TextReaderToStoreMain extends AbstractModule{
    
	private Conf conf;

	private TextReaderToStoreMain(final String[] args) throws ArgumentParserException, IOException {
		conf = new Args().toBean(args, Conf.class);
	}
	
	@Override
	protected void configure() {
		bind(ITextTransformer.class).to(NoOpTextTransformer.class);
	}
	
	public static void main(final String[] args) throws Exception {
		Guice.createInjector(new TextReaderToStoreMain(args)).getInstance(TextStore.class).start();
	}

	@Provides
	@Named("conf")
	@Singleton
	public Conf conf() {
		return conf;
	}

	@Provides
	@Named("finders")
	@Singleton
	public DatedPhrasesFinders finders(){
		return new DatedPhrasesFinders(conf.getNotInDateWords());
	}

}
