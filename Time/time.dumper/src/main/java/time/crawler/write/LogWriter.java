package time.crawler.write;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.conf.ConfKeys;

@Component
public class LogWriter implements IWriter {
	
    private static final Logger PAGEWRITER = LogManager.getLogger("pagestore");
    
	@Autowired
	public String logWriterPath;

    @Override
    public void writePage(final String url, final String title, final String text) {

        final StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(ConfKeys.sep);
        sb.append(title);
        sb.append(ConfKeys.sep);
        sb.append(text);
        sb.append(ConfKeys.sep);

       	PAGEWRITER.info(sb.toString());
    }
}
