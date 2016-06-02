package time.liveparse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.TextAnalyser;
import time.domain.*;
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
    private final TextAnalyser textAnalyser;
    private final String webRoot;
    private final String uploadDir;

    @Inject
    public LiveparseVerticle(Conf conf, TextAnalyser textAnalyser, ObjectMapper mapper, TextFactory textFactory) {
        this.port = conf.getPort();
        this.webRoot = conf.getWebRoot();
        this.uploadDir = conf.getUploadDir();
        if(!new File(this.webRoot).isDirectory()){
            throw new RuntimeException("incorrect webroot: " + this.webRoot);
        }
        if(!new File(this.uploadDir).isDirectory()){
            throw new RuntimeException("incorrect uploadDir: " + this.uploadDir);
        }
        this.textAnalyser = textAnalyser;
        this.mapper = mapper;
        this.textFactory = textFactory;

        LOGGER.info("port {}", port);
        LOGGER.info("webRoot {}", webRoot);
        LOGGER.info("uploadDir {}", uploadDir);
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setUploadsDirectory(uploadDir));
        router.get("/api/liveparse/url/:url").handler(this::fromUrl);
        router.route("/*").handler(StaticHandler.create().setCachingEnabled(false).setAllowRootFileSystemAccess(true).setWebRoot(webRoot));
        router.post("/api/liveparse/file").handler(this::fromFile);
        router.post("/api/liveparse/add").handler(this::add);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
        LOGGER.info("started");
    }

    private void add(RoutingContext ctx) {
        try {
            final Metadata metadata = mapper.readValue(ctx.getBodyAsString(), Metadata.class);
            mapper.writeValue(metadataFile, metadata);
            //TO BE CONTINUED
        } catch (IOException e) {
            LOGGER.error(e);
        }
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
        }, res -> ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .setChunked(true)
                    .write((String)res.result())
                    .end());
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
        final Text analysedText = textAnalyser.analyse(text);
        return mapper.writeValueAsString(toDTO(analysedText));
    }

    private String fileToText(final FileUpload file) throws JsonProcessingException, FileNotFoundException {
        final String filename = file.uploadedFileName();
        LOGGER.info("textFactory.build({})", filename);
        final Text text = textFactory.build(filename);
        LOGGER.info("textAnalyser.analyse(text)");
        final Text analysedText = textAnalyser.analyse(text);
        LOGGER.info("mapper.writeValueAsString(toDto(analysedTextDTO))");
        return mapper.writeValueAsString(toDTO(analysedText));
    }

    private TextDto toDTO(final Text analysedText) {
        final TextDto textDto = new TextDto();
        textDto.setText(analysedText.getHightlightTextString());
        textDto.setDatedPhrases(analysedText.getPhrases());
        textDto.setMetadata(analysedText.getMetadata());
        return textDto;
    }

}
