package time.liveparse;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.ConfEnum;
import time.domain.Analyser;
import time.domain.Conf;
import time.conf.ConfManager;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import javax.inject.Singleton;
import java.io.IOException;

public class LiveparseModule extends AbstractModule {

    private Conf conf;

    public LiveparseModule(String[] args) throws IOException, ArgumentParserException {
        conf = new ConfManager().get(args, Conf.class, "${TIME_HOME}/conf/liveparse.yml");
    }

    @Override
    protected void configure() {
        bind(ITextTransformer.class).to(CompositeTextTransformer.class);
    }

    @Provides @Singleton
    public Conf conf() {
        return conf;
    }

    @Provides @Singleton
    @Named("port")
    public int port() {
        return conf.getPort();
    }

    @Provides @Singleton
    public Analyser analyser() throws IOException {
        return new ConfManager().get(ConfEnum.ANALYSER, Analyser.class);
    }

}

