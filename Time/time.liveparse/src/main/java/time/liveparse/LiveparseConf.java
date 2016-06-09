package time.liveparse;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.conf.ConfEnum;
import time.domain.Analyser;
import time.conf.ConfManager;
import time.domain.Liveparse;
import time.domain.Transformer;
import time.transform.CompositeTextTransformer;
import time.transform.ITextTransformer;

import javax.inject.Singleton;
import java.io.IOException;

public class LiveparseConf extends AbstractModule {

    private Liveparse liveparse;

    public LiveparseConf(String[] args) throws IOException, ArgumentParserException {
        liveparse = new ConfManager().get(args, ConfEnum.LIVEPARSE, Liveparse.class);
    }

    @Override
    protected void configure() {
        bind(ITextTransformer.class).to(CompositeTextTransformer.class);
    }

    @Provides @Singleton
    public Liveparse liveparse() {
        return liveparse;
    }

    @Provides @Singleton
    @Named("port")
    public int port() {
        return liveparse.getPort();
    }

    @Provides @Singleton
    public Transformer transformer() throws IOException {
        return new ConfManager().get(ConfEnum.TRANSFORMER, Transformer.class);
    }

    @Provides @Singleton
    public Analyser analyser() throws IOException {
        return new ConfManager().get(ConfEnum.ANALYSER, Analyser.class);
    }

}

