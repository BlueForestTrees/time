package time.index.manage;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class IndexManageMain {

    private static final Logger LOGGER = LogManager.getLogger(IndexManageMain.class);

    public static void main(final String[] args) throws IOException, TimeoutException, ArgumentParserException {
        try {
            new IndexManageService(args);
        }catch(Exception e){
            LOGGER.error(e);
        }
    }

}
