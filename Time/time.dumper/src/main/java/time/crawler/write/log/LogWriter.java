package time.crawler.write.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.crawler.write.IWriter;
import time.crawler.write.Write;

public class LogWriter implements IWriter {
	
	private static final Logger LOGGER = LogManager.getLogger(LogWriter.class);
	
    private static final Logger PAGEWRITER = LogManager.getLogger("pagestore");
    
    @Inject
	public LogWriter(@Named("conf") final Conf conf){
		final String path = new LogWriterConf().logWriterPath(conf);
		LOGGER.info("write to " + path);
	}

    @Override
    public void writePage(final String url, final String title, final String metadata, final String text) {
       	PAGEWRITER.info(Write.concat(url, title, text));
    }

}
