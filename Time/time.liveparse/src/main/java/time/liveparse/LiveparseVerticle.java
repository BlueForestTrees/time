package time.liveparse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.liveparse.analyse.TextAnalyser;
import time.domain.Text;
import time.tika.ToText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LiveparseVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LogManager.getLogger(LiveparseVerticle.class);

    private final int port;
    private final ObjectMapper mapper;
    private final ToText toText;
    private final TextAnalyser analyser;
    private final String webRoot;

    @Inject
    public LiveparseVerticle(@Named("conf") Conf conf, TextAnalyser analyser, ObjectMapper mapper, ToText toText) {
        this.port = conf.getPort();
        this.webRoot = conf.getWebRoot();
        this.analyser = analyser;
        this.mapper = mapper;
        this.toText = toText;
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/*").handler(StaticHandler.create(webRoot).setCachingEnabled(false));
        router.post("/api/upload").handler(BodyHandler.create().setMergeFormAttributes(true));
        router.post("/api/upload").handler(this::upload);
        router.get("/api/fromurl").handler(this::fromUrl);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
        LOGGER.info("started");
    }

    private void upload(final RoutingContext ctx) {
        final FileUpload file = ctx.fileUploads().iterator().next();
        try {
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .setChunked(true)
                    .write(fileToText(file)).end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fromUrl(final RoutingContext ctx){
        final String url = ctx.get("url");
        try {
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .setChunked(true)
                    .write(urlToText(url)).end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String urlToText(final String url) throws IOException {
        return mapper.writeValueAsString(toDTO(analyser.analyse(toText.fromUrl(url))));
    }

    private String fileToText(final FileUpload file) throws JsonProcessingException, FileNotFoundException {
        return mapper.writeValueAsString(toDTO(analyser.analyse(toText.from(file.uploadedFileName()))));
    }

    private Map<String, Object> toDTO(final Text analyse) {
        final HashMap<String, Object> text = new HashMap<>();
        final HashMap<String, Object> metadatas = new HashMap<>();

        text.put("text", analyse.getHightlightTextString());
        text.put("metadatas", metadatas);
        metadatas.put("Titre", analyse.getTitle());
        metadatas.put("Auteur", analyse.getCreator());
        metadatas.put("Date", analyse.getCreated());
        metadatas.put("Paragraphes", analyse.getParagraphs().length);
        metadatas.put("Phrases datées", analyse.getPhrases().size());

        return text;
    }

}
