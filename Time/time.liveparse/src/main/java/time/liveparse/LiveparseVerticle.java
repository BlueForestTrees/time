package time.liveparse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.TextAnalyser;
import time.domain.*;
import time.messaging.Messager;
import time.messaging.Queue;
import time.tika.TextFactory;
import time.tool.string.Strings;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class LiveparseVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LogManager.getLogger(LiveparseVerticle.class);

    private final int port;
    private final String webRoot;
    private final String uploadDir;
    private final ObjectMapper mapper;
    private final TextFactory textFactory;
    private final TextAnalyser textAnalyser;
    private final Messager messager;

    @Inject
    public LiveparseVerticle(Liveparse conf, TextAnalyser textAnalyser, ObjectMapper mapper, TextFactory textFactory) throws IOException, TimeoutException {
        this.port = conf.getPort();
        this.webRoot = conf.getWebRoot();
        this.uploadDir = conf.getUploadDir();
        this.textAnalyser = textAnalyser;
        this.mapper = mapper;
        this.textFactory = textFactory;
        this.messager = new Messager();

        LOGGER.info("address http://localhost:{}", port);
        LOGGER.info("webRoot {}", webRoot);
        LOGGER.info("uploadDir {}", uploadDir);

        validateConf();
    }

    private void validateConf() {
        if(!new File(this.webRoot).isDirectory()){
            throw new RuntimeException("incorrect webroot: " + this.webRoot);
        }
        new File(this.uploadDir).mkdirs();
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create().setUploadsDirectory(uploadDir)).failureHandler(this::onFailure);

        router.get("/api/liveparse/url/:url").handler(this::fromUrl);
        router.post("/api/liveparse/file").handler(this::fromFile);
        router.post("/api/liveparse/add").handler(this::add);
        router.route("/*").handler(StaticHandler.create().setCachingEnabled(false).setAllowRootFileSystemAccess(true).setWebRoot(webRoot));

        vertx.createHttpServer().requestHandler(router::accept).listen(port);

        LOGGER.info("started");
    }

    private void onFailure(RoutingContext routingContext) {
        final StringWriter out = new StringWriter();
        routingContext.failure().printStackTrace(new PrintWriter(out));
        routingContext.response().setStatusCode(500).end(out.toString());
    }

    private void add(RoutingContext ctx) {
        try {
            final Metadata metadata = mapper.readValue(ctx.getBodyAsString(), Metadata.class);
            final String metapath = Strings.withSlash(uploadDir) + metadata.getFilename() + Metadata.EXT;
            final File metaFile = new File(metapath);

            LOGGER.info("writing file {} into {}", metadata, metapath);
            mapper.writeValue(metaFile, metadata);

            final Meta meta = new Meta(metapath);
            LOGGER.info("signaling meta {}", meta);
            messager.signal(Queue.META_CREATED, meta);
            ctx.response().end();
        } catch (IOException e) {
            LOGGER.error(e);
            ctx.fail(e);
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
        final Text text = textFactory.fromUrl(Strings.beginWith(url, "http://", "https://"), uploadDir);
        final Text analysedText = textAnalyser.analyse(text);
        return mapper.writeValueAsString(toDTO(analysedText));
    }

    private String fileToText(final FileUpload file) throws JsonProcessingException, FileNotFoundException {
        final String filepath = file.uploadedFileName();
        LOGGER.info("textFactory.fromFilepath({})", filepath);
        final Text text = textFactory.fromFilepath(filepath);
        //only to come back in doAdd() method for building metapath based on filepath
        text.getMetadata().setFilename(new File(filepath).getName());
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
