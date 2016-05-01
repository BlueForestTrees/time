package time.transformer;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.Args;
import time.conf.Conf;
import time.transformer.page.transformer.IPageTransformer;
import time.transformer.page.transformer.NoOpPageTransformer;
import time.transformer.phrase.finder.DateFindersFactory;
import time.transformer.phrase.finder.PhraseFinder;

public class TransformerTxtSepBatchMain extends AbstractModule{
    
	private Conf conf;

	private TransformerTxtSepBatchMain(final String[] args) throws ArgumentParserException, IOException {
		conf = new Args().toBean(args, Conf.class);
	}
	
	@Override
	protected void configure() {
		bind(IPageTransformer.class).to(NoOpPageTransformer.class);
	}
	
	public static void main(final String[] args) throws Exception {
		Guice.createInjector(new TransformerTxtSepBatchMain(args)).getInstance(TransformerTxtSepBatch.class).start();
	}

	@Provides
	@Named("conf")
	@Singleton
	public Conf conf() {
		return conf;
	}

	@Provides
	@Named("notInDateWords")
	@Singleton
	public String notInDateWords(){
		return conf.getNotInDateWords();
	}

	@Provides
	@Named("finders")
	@Singleton
	public PhraseFinder[] finders(@Named("notInDateWords") final String notInDateWords){
		return new DateFindersFactory(notInDateWords).finders();
	}

}