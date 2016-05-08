package time.analyser;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.analyse.TextAnalyser;
import time.conf.Args;
import time.conf.Conf;
import time.repo.bean.Text;
import time.tika.ToText;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SimpleREST extends AbstractVerticle {


    private static final Logger LOGGER = LogManager.getLogger(SimpleREST.class);

    private final int port;

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
    public SimpleREST(@Named("port") int port, TextAnalyser analyser, ObjectMapper mapper, ToText toText) {
        this.port = port;
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
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
        LOGGER.info("started");
    }

    private void upload(RoutingContext ctx) {
        final FileUpload file = ctx.fileUploads().iterator().next();
        try {
            ctx.response().putHeader("Content-Type", "application/json")
                    .setChunked(true)
                    .write(mapper.writeValueAsString(toDTO(analyser.analyse(toText.from(file.uploadedFileName()))))).end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> toDTO(Text analyse) {
        final HashMap<String, String> text = new HashMap<>();

        text.put("text", analyse.getHightlightTextString());

        return text;
    }

}
