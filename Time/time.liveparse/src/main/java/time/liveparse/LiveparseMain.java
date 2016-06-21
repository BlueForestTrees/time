package time.liveparse;


import io.vertx.core.Vertx;
import time.guice.GuiceVerticleFactory;

public class LiveparseMain {


    public static void main(String[] args) throws Exception {
        final Vertx vertx = Vertx.vertx();
        vertx.registerVerticleFactory(new GuiceVerticleFactory(new LiveparseModule(args)));
        vertx.deployVerticle("guice:" + LiveparseVerticle.class.getName());
    }

}
