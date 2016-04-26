package time.merger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import time.conf.Conf;
import time.merger.service.MergeIndexService;

import java.io.IOException;

public class MergerMain extends AbstractModule {

    private Conf conf;

    private MergerMain(final String[] args) throws ArgumentParserException, IOException {
        conf = new Args().toBean(args, Conf.class);
    }

    @Override
    protected void configure() {
        bind(IPageTransformer.class).to(NoOpPageTransformer.class);
    }

    public static void main(final String[] args) throws Exception {
        Guice.createInjector(new MergerMain(args)).getInstance(MergeIndexService.class).merge();
    }

    @Provides
    @Named("conf")
    @Singleton
    public Conf conf() {
        return conf;
    }

	private void execute() throws IOException {
        final String src = conf.getSrc();
        final String dest = conf.getDest();
		LOG.info("mergeIndexService.merge("+src+","+dest+")");
		mergeIndexService.merge(src, dest);
	}
    


}
