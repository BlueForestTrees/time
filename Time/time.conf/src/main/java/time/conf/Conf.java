package time.conf;

import time.tool.string.Strings;

import java.util.Arrays;
import java.util.List;

public class Conf {
	private String webRoot;
	private String target;
	private String home;
	private String homeDir;
	private int port;
	private String basePath;
	private String name;
	private String baseUrl;
	private String regexUrlFilter;
	private String urlFilter;
	private String storagePath;
	private Long nbPageLog = 1000L;
    private Long nbPhraseLog = 10000L;
	private int maxPages = -1;
	private int politenessDelay = 25;
	private String crawlStorageFolder;
	private boolean resumable = false;
	private String seedUrl;
	private int nbCrawlers = 5;
	private long pageSize = 100;
	private String maxFileSize = "100MB";
	private String type;
	private List<String> contentExclusion;
	private String source;
	private String url;
	private String title;
	private String includePattern;
	private String excludePattern;
	private String sourceDir;
	private String txtOutputFile;
	private String txtOutputDir;
	private String crawlStorageDir;
	private long chronoPageTotal;
	private long maxPhrasesToFetch;
	private int maxLength = 1000;
	private int minLength = 40;
	private List<String> excludeAfterList;
	private int urlMaxLength = 255;
	private List<String> urlMustNotContain = Arrays.asList();
	private String sourcePath;
	private String datasourcePath;
	private String indexDir;
	private String finalIndexDir;
	private String[] phraseMustNotStartWith;
	private String mergeableIndexesDir;
	private String mergedIndexDir;

	public String getMergedIndexDir() {
		return mergedIndexDir;
	}
	public String getMergeableIndexesDir() {
		return mergeableIndexesDir;
	}
	public String getFinalIndexDir() {
		return finalIndexDir;
	}
	public String getWebRoot() {
		return webRoot;
	}
	public int getPort() {
		return port;
	}
	public String getTarget() {
		return Strings.withSlash(target);
	}
	public String getSourceDir(){
		return sourceDir;
	}
	public String getCrawlStorageDir() {
		return crawlStorageDir;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public long getNbPageLog() {
		return nbPageLog;
	}
	public int getMaxPages() {
		return maxPages;
	}
	public boolean isResumable() {
		return resumable;
	}
	public String getSeedUrl() {
		return seedUrl;
	}
	public int getNbCrawlers() {
		return nbCrawlers;
	}
	public String getType() {
		return type;
	}
	public List<String> getContentExclusion() {
		return contentExclusion;
	}
	public String getSource() {
		return source;
	}
	public String getUrl() {
		return url;
	}
	public String getTitle() {
		return title;
	}
	public String getIncludePattern() {
		return includePattern;
	}
	public String getName() {
		return name;
	}
	public String getUrlFilter() {
		return urlFilter;
	}
	public int getPolitenessDelay() {
		return politenessDelay;
	}
	public long getChronoPageTotal() {
		return chronoPageTotal;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public int getMinLength() {
		return minLength;
	}
	public List<String> getExcludeAfterList() {
		return excludeAfterList;
	}
	public int getUrlMaxLength() {
		return urlMaxLength;
	}
	public List<String> getUrlMustNotContain() {
		return urlMustNotContain;
	}
	public String getIndexDir() {
		return indexDir;
	}
	public String[] getPhraseMustNotStartWith() {
		return phraseMustNotStartWith;
	}
    public Long getNbPhraseLog() {
        return nbPhraseLog;
    }
}
