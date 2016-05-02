package time.transformer.read;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;

import time.repo.bean.Text;

public class TextReader {

    private static final Logger logger = LogManager.getLogger(TextReader.class);

    private SmartScanner scanner;
    
    @Inject
    public TextReader(final SmartScanner scanner){
    	this.scanner = scanner;
    }

    public Text getNextText() throws IOException, FinDuScanException {
        final String url = scanner.nextString();
        final String title = scanner.nextString();
        final String text = scanner.nextString();

        if (logger.isDebugEnabled()) {
            logger.debug("url=" + url);
            logger.debug("title=" + title);
            logger.debug("text=" + text.substring(0, 50) + "[...]" + text.substring(text.length() - 50));
        }

        final Text page = new Text();
        page.setUrl(url);
        page.setTitle(title);
        page.setText(text);

        return page;
    }

}
