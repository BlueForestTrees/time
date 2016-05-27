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
import time.analyser.TextAnalyser;
import time.domain.DatedPhrase;
import time.domain.Text;
import time.tika.TextFactory;
import time.tool.string.Strings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveparseVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LogManager.getLogger(LiveparseVerticle.class);

    private final int port;
    private final ObjectMapper mapper;
    private final TextFactory textFactory;
    private final TextAnalyser analyser;
    private final String webRoot;

    @Inject
    public LiveparseVerticle(Conf conf, TextAnalyser analyser, ObjectMapper mapper, TextFactory textFactory) {
        this.port = conf.getPort();
        this.webRoot = conf.getWebRoot();
        if(!new File(this.webRoot).isDirectory()){
            throw new RuntimeException("incorrect webroot: " + this.webRoot);
        }
        this.analyser = analyser;
        this.mapper = mapper;
        this.textFactory = textFactory;
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/api/liveparse/url/:url").handler(this::fromUrl);
        router.route("/*").handler(StaticHandler.create().setCachingEnabled(false).setAllowRootFileSystemAccess(true).setWebRoot(webRoot));
        router.post("/api/liveparse/file").handler(this::fromFile);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
        LOGGER.info("started");
    }

    private void fromFile(final RoutingContext ctx) {
        vertx.executeBlocking(future -> {
            try {
                final FileUpload file = ctx.fileUploads().iterator().next();
                LOGGER.info("fromFile {}", file.fileName());
                final String text = fileToText(file);
                LOGGER.info("fromFile {} done" , file.fileName());
                future.complete(text);
            } catch (Exception e) {
                ctx.fail(e);
            }
        }, res -> {
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .setChunked(true)
                    .write((String)res.result())
                    .end();
        });
    }

    private void fromUrl(final RoutingContext ctx){
        try {
            final String url = ctx.request().getParam("url");
            LOGGER.info("fromUrl " + url);
            final String text = urlToText(url);
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .setChunked(true)
                    .write(text)
                    .end();
        } catch (Exception e) {
            ctx.fail(e);
        }
    }

    private String urlToText(final String url) throws IOException {
        final Text text = textFactory.buildFromUrl(Strings.beginWith(url, "http://", "https://"));
        final Text analysedText = analyser.analyse(text);
        final Map<String, Object> analysedTextDTO = toDTO(analysedText);
        return mapper.writeValueAsString(analysedTextDTO);
    }

    private String fileToText(final FileUpload file) throws JsonProcessingException, FileNotFoundException {
        final String filename = file.uploadedFileName();
        LOGGER.info("textFactory.build({})", filename);
        final Text text = textFactory.build(filename);
        LOGGER.info("analyser.analyse(text)");
        final Text analysedText = analyser.analyse(text);
        LOGGER.info("toDTO(analysedText)");
        final Map<String, Object> analysedTextDTO = toDTO(analysedText);
        LOGGER.info("mapper.writeValueAsString(analysedTextDTO)");
        final String analysedTextDTOString = mapper.writeValueAsString(analysedTextDTO);

        return analysedTextDTOString;
    }

    private Map<String, Object> toDTO(final Text analysedText) {
        final HashMap<String, Object> dto = new HashMap<>();

        final String text = analysedText.getHightlightTextString();
        final HashMap<String, Object> metadatas = new HashMap<>();
        final List<DatedPhrase> datedPhrases = analysedText.getPhrases();

        dto.put("text", text);
        dto.put("metadatas", metadatas);
        dto.put("datedPhrases", datedPhrases);

        metadatas.put("Titre", analysedText.getTitle());
        metadatas.put("Auteur", analysedText.getCreator());
        metadatas.put("Date", analysedText.getCreated());
        metadatas.put("Paragraphes", analysedText.getParagraphs().length);
        metadatas.put("Phrases datées", analysedText.getPhrases().size());


        return dto;
    }

}
