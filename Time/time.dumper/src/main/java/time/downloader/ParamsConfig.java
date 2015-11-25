package time.downloader;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamsConfig {

    private static final Logger LOGGER = LogManager.getLogger(ParamsConfig.class);
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

    @Autowired
    private ApplicationArguments args;

    @Bean
    public Values config() {
        final Values config = new Values();
        final String[] sourceArgs = args.getSourceArgs();

        for (int i = 0; i < sourceArgs.length; i += 2) {
            final String key = sourceArgs[i];
            final String value = sourceArgs[i + 1];
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
        }
        return config;
    }

    public class Values {

        private String sep;
        private Pattern filter;
        private String[] excludeFilter;
        private int nbPageLog;
        private String seedUrl;
        private int nbCrawlers;
        private int delay;
        private int maxPages;
        private String crawlPath;
        private boolean resumable;
        private String baseUrl;
        private boolean write;
        private boolean help;
        private String storagePath;
        private String maxFileSize;

        public Values() {
            this.setMaxPages(-1);
            this.setStoragePath("C:/Time/data/pages");
            this.setNbPageLog(1000);
            this.setWrite(true);
            this.setSeedUrl("http://fr.wikipedia.org/wiki/Portail:Accueil");
            this.setDelay(1);
            this.setNbCrawlers(50);
            this.setCrawlPath("C:/Time/crawldata");
            this.setBaseUrl("https://fr.wikipedia.org/wiki");
            this.setResumable(false);
            this.setHelp(false);
            this.setMaxFileSize("100MB");
            this.setFilter(Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|svg|ogg|ogv|oga|djvu|webm))$"));
            this.setSep("|¨");
        }

        public String[] getExcludeFilter() {
            return excludeFilter;
        }

        public void setExcludeFilter(String[] excludeFilter) {
            this.excludeFilter = excludeFilter;
        }

        public String getSep() {
            return sep;
        }

        public void setSep(String sep) {
            this.sep = sep;
        }

        public Pattern getFilter() {
            return filter;
        }

        public void setFilter(Pattern filter) {
            this.filter = filter;
        }

        public String getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(String maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public String getStoragePath() {
            return storagePath;
        }

        public void setStoragePath(String storagePath) {
            this.storagePath = storagePath;
        }

        public boolean isHelp() {
            return help;
        }

        public void setHelp(boolean help) {
            this.help = help;
        }

        public boolean isWrite() {
            return write;
        }

        public void setWrite(boolean write) {
            this.write = write;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public int getNbPageLog() {
            return nbPageLog;
        }

        public void setNbPageLog(int nbPageLog) {
            this.nbPageLog = nbPageLog;
        }

        public String getSeedUrl() {
            return seedUrl;
        }

        public void setSeedUrl(String seedUrl) {
            this.seedUrl = seedUrl;
        }

        public int getNbCrawlers() {
            return nbCrawlers;
        }

        public void setNbCrawlers(int nbCrawlers) {
            this.nbCrawlers = nbCrawlers;
        }

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public int getMaxPages() {
            return maxPages;
        }

        public void setMaxPages(int maxPages) {
            this.maxPages = maxPages;
        }

        public String getCrawlPath() {
            return crawlPath;
        }

        public void setCrawlPath(String crawlPath) {
            this.crawlPath = crawlPath;
        }

        public boolean isResumable() {
            return resumable;
        }

        public void setResumable(boolean resumable) {
            this.resumable = resumable;
        }

        public String getConfAsString() {
            StringBuilder sb = new StringBuilder();

            sb.append("\n");
            sb.append("\n----------CRAWL CONFIG-------------");
            sb.append("\nseedUrl=" + getSeedUrl());
            sb.append("\nnbCrawlers=" + getNbCrawlers());
            sb.append("\nmaxPages=" + getMaxPages());
            sb.append("\ndelay=" + getDelay());
            sb.append("\ncrawlPath=" + getCrawlPath());
            sb.append("\nbaseUrl=" + getBaseUrl());
            sb.append("\nnbPageLog=" + getNbPageLog());
            sb.append("\nresumable=" + isResumable());
            sb.append("\nwrite=" + isWrite());
            sb.append("\nstoragePath=" + getStoragePath());
            sb.append("\nmaxFileSize=" + getMaxFileSize());
            sb.append("\n--------END CRAWL CONFIG-----------");
            sb.append("\n");

            return sb.toString();
        }

    }

}
