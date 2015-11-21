package time.dumper;




import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.dumper.config.DumperConfig;
import time.dumper.factory.DumperFactory;

@Configuration
public class Params {

    private static final Logger LOGGER = Logger.getLogger(DumperFactory.class);
    private static final String HISTOIRE_ARG_PREFIX = "-H";
    private static final String SEED_URL = "seedUrl";
    private static final String NB_CRAWLERS = "nbCrawlers";
    private static final String MAX_PAGES = "maxPages";
    private static final String DELAY = "delay";
    private static final String CRAWL_PATH = "crawlPath";
    private static final String BASE_URL = "baseUrl";
    private static final String NB_PAGE_LOG = "nbPageLog";
    private static final String RESUMABLE = "resumable";
    private static final String WRITE = "write";
    private static final String HELP = "help";
    private static final String STORAGE_PATH = "storagePath";
    private static final String MAX_FILE_SIZE = "maxFileSize";
    
    @Bean
    public DumperConfig config(ApplicationArguments args) {
        final DumperConfig config = new DumperConfig();
        
        String[] sourceArgs = args.getSourceArgs();
        
        for (int i = 0; i < sourceArgs.length; i++) {
            String arg = sourceArgs[i];
            if (arg.startsWith(HISTOIRE_ARG_PREFIX)) {
                final String key = arg.replace(HISTOIRE_ARG_PREFIX, "");
                i++;
                String value = sourceArgs[i];
                if (key.equalsIgnoreCase(SEED_URL)) {
                    config.setSeedUrl(value);
                } else if (key.equalsIgnoreCase(NB_CRAWLERS)) {
                    config.setNbCrawlers(Integer.parseInt(value));
                } else if (key.equalsIgnoreCase(DELAY)) {
                    config.setDelay(Integer.parseInt(value));
                } else if (key.equalsIgnoreCase(MAX_PAGES)) {
                    config.setMaxPages(Integer.parseInt(value));
                } else if (key.equalsIgnoreCase(CRAWL_PATH)) {
                    config.setCrawlPath(value);
                } else if (key.equalsIgnoreCase(BASE_URL)) {
                    config.setBaseUrl(value);
                } else if (key.equalsIgnoreCase(NB_PAGE_LOG)) {
                    config.setNbPageLog(Integer.parseInt(value));
                } else if (key.equalsIgnoreCase(RESUMABLE)) {
                    config.setResumable(Boolean.parseBoolean(value));
                } else if (key.equalsIgnoreCase(WRITE)) {
                    config.setWrite(Boolean.parseBoolean(value));
                } else if (key.equalsIgnoreCase(HELP)) {
                    config.setHelp(Boolean.parseBoolean(value));
                } else if (key.equalsIgnoreCase(STORAGE_PATH)) {
                    config.setStoragePath(value);
                } else if (key.equalsIgnoreCase(MAX_FILE_SIZE)) {
                    config.setMaxFileSize(value);
                } else {
                    LOGGER.warn("unknown parameter: " + key + "=" + value);
                }
            } else {
                LOGGER.warn("unknown parameter: " + arg);
            }
        }
        return config;
    }
    
}
