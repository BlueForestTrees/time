package time.downloader;

import java.io.File;
import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.downloader.writer.PageLayout;

@Component
public class StorageConfig {

    private static final String PAGESTORE = "pagestore";
    
    @Autowired
    private DownloaderConfig.Values config;
    
    public String configureStorage() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration logConfig = ctx.getConfiguration();
        final String fileName = config.getStoragePath();
        final String filePattern = config.getStoragePath() + "%i";
        final String append = "false";
        final String immediateFlush = "true";
        final String bufferSizeStr = String.valueOf(RollingRandomAccessFileManager.DEFAULT_BUFFER_SIZE);
        final TriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(config.getMaxFileSize());
        final RolloverStrategy strategy = DefaultRolloverStrategy.createStrategy("1000", null, null, null, logConfig);
        final Layout<? extends Serializable> layout = new PageLayout();

        createLogFolderIfNeeded(fileName);
        final RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.createAppender(fileName, filePattern, append, PAGESTORE, immediateFlush, bufferSizeStr, policy, strategy, layout, null, "true", "true", null, logConfig);

        appender.start();
        logConfig.addAppender(appender);

        final AppenderRef ref = AppenderRef.createAppenderRef(PAGESTORE, null, null);
        final AppenderRef[] refs = new AppenderRef[] { ref };
        final LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.INFO, PAGESTORE, "true", refs, null, logConfig, null);
        loggerConfig.addAppender(appender, null, null);
        logConfig.addLogger(PAGESTORE, loggerConfig);
        ctx.updateLoggers();
        return "storage done";
    }

	private void createLogFolderIfNeeded(final String fileName) {
		new File(fileName).mkdirs();
	}
}
