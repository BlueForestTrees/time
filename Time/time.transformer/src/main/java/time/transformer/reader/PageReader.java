package time.transformer.reader;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;

import time.repo.bean.Page;

public class PageReader {

    private static final Logger logger = LogManager.getLogger(PageReader.class);

    private SmartScanner scanner;
    
    @Inject
    public PageReader(final SmartScanner scanner){
    	this.scanner = scanner;
    }

    public Page getNextPage() throws IOException, FinDuScanException {
        final String url = scanner.nextString();
        final String title = scanner.nextString();
        final String metadata = scanner.nextString();
        final String text = scanner.nextString();

        if (logger.isDebugEnabled()) {
            logger.debug("url=" + url);
            logger.debug("title=" + title);
            logger.debug("metadata=" + metadata);
            logger.debug("text=" + text.substring(0, 50) + "[...]" + text.substring(text.length() - 50));
        }

        final Page page = new Page();
        page.setUrl(url);
        page.setText(text);

        return page;
    }

}
