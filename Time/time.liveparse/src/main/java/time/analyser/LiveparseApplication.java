package time.analyser;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.analyser.find.DatedPhrasesFinders;
import time.conf.Args;
import time.conf.Conf;

import java.io.IOException;

public class LiveparseApplication extends AbstractModule {

    private Conf conf;

    public LiveparseApplication(String[] args) throws IOException, ArgumentParserException {
        conf = new Args().toBean(args, Conf.class);
    }

    public static void main(String[] args) throws Exception {
        Guice.createInjector(new LiveparseApplication(args)).getInstance(LiveparseServer.class).start();
    }

    @Override
    protected void configure() {

    }

    @Provides @Singleton @Named("conf")
    public Conf conf() {
        return conf;
    }

    @Provides @Singleton @Named("port")
    public int port() {
        return conf.getPort();
    }

    @Provides @Singleton @Named("finders")
    public DatedPhrasesFinders finders(){
        return new DatedPhrasesFinders(conf.getNotInDateWords());
    }
}

