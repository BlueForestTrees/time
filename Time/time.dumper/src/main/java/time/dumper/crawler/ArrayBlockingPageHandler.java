package time.dumper.crawler;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import time.tool.chrono.Chrono;
import edu.uci.ics.crawler4j.crawler.Page;

public class ArrayBlockingPageHandler extends ThreadedHandler {

    protected static final ArrayBlockingQueue<Page> pages = new ArrayBlockingQueue<Page>(100);
    private static final Logger LOG = Logger.getLogger(ArrayBlockingPageHandler.class);

    @Override
    public void visit(Page page) {
        try {
            pages.put(page);
        } catch (InterruptedException e) {
            LOG.error("ArrayBlockingWriterCrawler:pages.put(page);", e);
        }
    }

    @Override
    public void run() {
        LOG.info("demarrage");
        pageCount = 0;
        Chrono chrono = null;
        if (LOG.isDebugEnabled()) {
            chrono = new Chrono("Writer");
            chrono.start();
        }

        while (!isEnd() || pages.isEmpty()) {
            try {
                Page page = pages.take();
                writer.writePage(page);
                pageCount++;
                if (LOG.isDebugEnabled() && (pageCount % nbPageLog == 0)) {
                    chrono.stop();
                    LOG.debug("[" + pages.size() + "] page#" + pageCount + ": " + chrono.toString());
                    chrono.start();
                }
            } catch (InterruptedException e) {
                LOG.error("Writer:pages.take();", e);
            }
        }
        if (LOG.isDebugEnabled()) {
            chrono.stop();
            LOG.debug("page#" + pageCount + ": " + chrono.toString());
        }
        LOG.info("arret");
    }
}
