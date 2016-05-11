package time.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

import java.util.Arrays;

public class GuiceVerticleFactory implements VerticleFactory {

    public GuiceVerticleFactory(Module module) {
        this.module = module;
    }

    private Module module;

    @Override
    public String prefix() {
        return "guice";
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        final Injector injector = Guice.createInjector(Arrays.asList(module));
        final Class clazz = classLoader.loadClass(VerticleFactory.removePrefix(verticleName));
        return (Verticle) injector.getInstance(clazz);
    }
}
