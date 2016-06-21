package time.liveparse;


import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.guice.GuiceVerticleFactory;

public class LiveparseMain {


    private static final Logger LOGGER = LogManager.getLogger(LiveparseMain.class);

    public static void main(String[] args) {
        try {
            LOGGER.info("Working Directory = " + System.getProperty("user.dir"));
            LOGGER.info("final Vertx vertx = Vertx.vertx(); ...");
            final Vertx vertx = Vertx.vertx();
            LOGGER.info("final LiveparseModule module = new LiveparseModule(args); ...");
            final LiveparseModule module = new LiveparseModule(args);
            LOGGER.info("final GuiceVerticleFactory factory = new GuiceVerticleFactory(module); ...");
            final GuiceVerticleFactory factory = new GuiceVerticleFactory(module);
            LOGGER.info("vertx.registerVerticleFactory(factory); ...");
            vertx.registerVerticleFactory(factory);
            LOGGER.info("vertx.deployVerticle(\"guice:\" + LiveparseVerticle.class.getName());");
            vertx.deployVerticle("guice:" + LiveparseVerticle.class.getName());
        }catch(Exception e){
            LOGGER.error(e);
        }
    }

}
