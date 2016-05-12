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
import time.tool.string.Strings;

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
        router.get("/api/fromurl/:url").handler(this::fromUrl);
        router.route().handler(BodyHandler.create());
        router.route("/*").handler(StaticHandler.create(webRoot).setCachingEnabled(false));
        router.post("/api/upload").handler(BodyHandler.create().setMergeFormAttributes(true));
        router.post("/api/upload").handler(this::upload);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
        LOGGER.info("started");
    }

    private void upload(final RoutingContext ctx) {
        try {
            final FileUpload file = ctx.fileUploads().iterator().next();
            ctx.response()
                    .setChunked(true)
                    .write(fileToText(file))
                    .putHeader("Content-Type", "application/json")
                    .end();
        } catch (Exception e) {
            ctx.fail(e);
        }
    }

    private void fromUrl(final RoutingContext ctx){
        try {
            final String url = ctx.request().getParam("url");
            ctx.response()
                    .setChunked(true)
                    .write(urlToText(url))
                    .putHeader("Content-Type", "application/json")
                    .end();
        } catch (Exception e) {
            ctx.fail(e);
        }
    }

    private String urlToText(final String url) throws IOException {
        final Text text = toText.fromUrl(Strings.beginWith(url, "http://", "https://"));
        final Text analysedText = analyser.analyse(text);
        final Map<String, Object> analysedTextDTO = toDTO(analysedText);
        return mapper.writeValueAsString(analysedTextDTO);
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
