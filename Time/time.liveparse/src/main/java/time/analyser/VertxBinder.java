package time.analyser;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;

class VertxBinder extends AbstractModule {

    private final Vertx vertx;

    public VertxBinder(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(vertx);
    }
}