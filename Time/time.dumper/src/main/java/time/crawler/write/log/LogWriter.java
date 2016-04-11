package time.crawler.write.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.crawler.write.IWriter;
import time.crawler.write.Write;

@Component
public class LogWriter implements IWriter {
	
    private static final Logger PAGEWRITER = LogManager.getLogger("pagestore");
    
	@Autowired
	public String logWriterPath;

    @Override
    public void writePage(final String url, final String title, final String text) {
       	PAGEWRITER.info(Write.concat(url, title, text));
    }

}
