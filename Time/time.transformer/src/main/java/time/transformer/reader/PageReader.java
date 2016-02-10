package time.transformer.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.repo.bean.Page;
import time.transformer.reader.FinDuScanException;
import time.transformer.reader.SmartScanner;

@Service
public class PageReader {

    private static final Logger logger = LogManager.getLogger(PageReader.class);

    @Autowired
    private String baseUrl;

    @Autowired
    private SmartScanner scanner;

    public Page getNextPage() throws IOException, FinDuScanException {
        String url = scanner.nextString();
        int depth = scanner.nextInt();
        String title = scanner.nextString();
        int nbLiensOut = scanner.nextInt();
        List<String> liens = new ArrayList<String>();

        if (logger.isDebugEnabled()) {
            logger.debug("url=" + url);
            logger.debug("depth=" + depth);
            logger.debug("title=" + title);
            logger.debug("nbLiensOut=" + nbLiensOut);
        }

        for (int i = 1; i <= nbLiensOut; i++) {
            String lien = scanner.nextString();
            if (startWithBaseUrl(lien)) {
                liens.add(reduceThisLink(lien));
                if (logger.isDebugEnabled()) {
                    logger.debug("lien#" + i + "=" + lien);
                }
            }
        }
        String text = scanner.nextString();

        Page page = new Page();
        page.setLiens(liens);
        page.setUrl(reduceThisLink(url));
        page.setDepth(depth);
        page.setNbLiensOut(nbLiensOut);
        page.setText(text);

        if (logger.isDebugEnabled()) {
            logger.debug("text=" + text.substring(0, 50) + "[...]" + text.substring(text.length() - 50));
        }

        return page;
    }

    private String reduceThisLink(String lien) {
        return lien.substring(baseUrl.length());
    }

    private boolean startWithBaseUrl(String lien) {
        return lien.startsWith(baseUrl);
    }

}
