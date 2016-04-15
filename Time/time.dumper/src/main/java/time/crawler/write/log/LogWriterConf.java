package time.crawler.write.log;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import time.crawler.conf.Conf;

public class LogWriterConf {
	
    private static final String PAGESTORE = "pagestore";
    
	public String logWriterPath(final Conf conf) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final org.apache.logging.log4j.core.config.Configuration logConfig = ctx.getConfiguration();
        final String filename = conf.getPagesFile1();
        final String filePattern = conf.getPagesFile1() + "%i";
        final String append = "false";
        final String immediateFlush = "true";
        final String bufferSizeStr = String.valueOf(RollingRandomAccessFileManager.DEFAULT_BUFFER_SIZE);
        final TriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(conf.getMaxFileSize());
        final RolloverStrategy strategy = DefaultRolloverStrategy.createStrategy("1000", null, null, null, logConfig);
        final AbstractStringLayout layout = getLayout();

        new File(filename).mkdirs();
        final RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.createAppender(filename, filePattern, append, PAGESTORE, immediateFlush, bufferSizeStr, policy, strategy, layout, null, "true", "true", null, logConfig);

        appender.start();
        logConfig.addAppender(appender);

        final AppenderRef ref = AppenderRef.createAppenderRef(PAGESTORE, null, null);
        final AppenderRef[] refs = new AppenderRef[] { ref };
        final LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.INFO, PAGESTORE, "true", refs, null, logConfig, null);
        loggerConfig.addAppender(appender, null, null);
        logConfig.addLogger(PAGESTORE, loggerConfig);
        ctx.updateLoggers();
        
        return filename;
    }

	private AbstractStringLayout getLayout() {
		return new AbstractStringLayout(Charset.defaultCharset()){
			private static final long serialVersionUID = 2961171351622684601L;
			@Override
			public String toSerializable(final LogEvent event) {
				return event.getMessage().getFormattedMessage();
			}
		};
	}
}
