package time.dump.wiki;

import com.google.inject.Guice;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DumpWikiMain {

    private static final Logger LOGGER = LogManager.getLogger(DumpWikiMain.class);

    private DumpWikiMain(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        try {
            Guice.createInjector(new DumpWikiModule(args)).getInstance(DumpWiki.class).crawl();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static void main(final String[] args) throws IOException, ArgumentParserException, TimeoutException {
        new DumpWikiMain(args);
    }

}
