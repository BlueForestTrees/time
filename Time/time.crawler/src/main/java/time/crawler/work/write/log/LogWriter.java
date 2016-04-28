package time.crawler.work.write.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.crawler.work.write.IWriter;
import time.crawler.work.write.Write;
import time.repo.bean.Page;

public class LogWriter implements IWriter {
	
	private static final Logger LOGGER = LogManager.getLogger(LogWriter.class);
	
    private static final Logger PAGEWRITER = LogManager.getLogger("pagestore");
    
    @Inject
	public LogWriter(@Named("conf") final Conf conf){
		final String path = new LogWriterConf().logWriterPath(conf);
		LOGGER.info("write to " + path);
	}

    @Override
    public void writePage(final Page page) {
       	PAGEWRITER.info(Write.concat(page.getUrl(), page.getTitle(), page.getTextString()));
    }

}
