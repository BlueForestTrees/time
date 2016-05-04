package time.analyser;

import com.englishtown.vertx.guice.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.vertx.core.Verticle;
import io.vertx.core.impl.verticle.CompilingClassLoader;
import io.vertx.core.spi.VerticleFactory;

import java.util.ArrayList;
import java.util.List;

public class MyVerticleFactory implements VerticleFactory {

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    private Module module = null;

    @Override
    public String prefix() {
        return "guice";
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        verticleName = VerticleFactory.removePrefix(verticleName);
        Class clazz;
        if (verticleName.endsWith(".java")) {
            CompilingClassLoader compilingLoader = new CompilingClassLoader(classLoader, verticleName);
            String className = compilingLoader.resolveMainClassName();
            clazz = compilingLoader.loadClass(className);
        } else {
            clazz = classLoader.loadClass(verticleName);
        }

        List<Module> modules = new ArrayList<>();
        modules.add(module);

        Injector injector = Guice.createInjector(modules);
        return (Verticle) injector.getInstance(clazz);
    }
}
