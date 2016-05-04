package time.analyser;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import time.analyser.analyse.TextAnalyser;
import time.tika.ToText;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SimpleREST extends AbstractVerticle {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException, IOException, ArgumentParserException {
        final MyVerticleFactory factory = new MyVerticleFactory();
        factory.setModule(new LiveparseApplication(args));
        Vertx vertx = Vertx.vertx();
        vertx.registerVerticleFactory(factory);
        vertx.deployVerticle("guice:" + SimpleREST.class.getName());
    }

    final ObjectMapper mapper;
    final ToText toText;
    final TextAnalyser analyser;

    @Inject
    public SimpleREST(TextAnalyser analyser, ObjectMapper mapper, ToText toText) {
        this.analyser = analyser;
        this.mapper = mapper;
        this.toText = toText;
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/*").handler(StaticHandler.create());
        router.post("/api/upload").handler(BodyHandler.create().setMergeFormAttributes(true));
        router.post("/api/upload").handler(this::upload);
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void upload(RoutingContext ctx) {
        final FileUpload file = ctx.fileUploads().iterator().next();
        try {
            ctx.response().putHeader("Content-Type", "application/json")
                    .setChunked(true)
                    .write(mapper.writeValueAsString(analyser.analyse(toText.from(file.uploadedFileName())))).end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
