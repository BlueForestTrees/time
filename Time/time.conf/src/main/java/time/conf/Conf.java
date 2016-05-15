package time.conf;

import time.tool.string.Strings;

import java.util.Arrays;
import java.util.List;

public class Conf {
	public static final String sep = "\\|¨";

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
	private String notInDateWords;

	public String getFinalIndexDir() {
		return finalIndexDir;
	}

	public void setFinalIndexDir(String finalIndexDir) {
		this.finalIndexDir = finalIndexDir;
	}

	public String getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	public String getHomeDir() {
		return homeDir;
	}

	public int getPort() {
		return port;
	}
	public String getNotInDateWords() { return notInDateWords; }

	public String getHome() {
		return Strings.withSlash(home);
	}

	public String getTarget() {
		return Strings.withSlash(target);
	}

	public String getSourceDir(){
		return sourceDir;
	}

	public String getTxtOutputDir() {
		return txtOutputDir;
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

	public String getCrawlStorageFolder() {
		return crawlStorageFolder;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getChronoPageTotal() {
		return chronoPageTotal;
	}

	public void setChronoPageTotal(long chronoPageTotal) {
		this.chronoPageTotal = chronoPageTotal;
	}

	public long getMaxPhrasesToFetch() {
		return maxPhrasesToFetch;
	}

	public void setMaxPhrasesToFetch(long maxPhrasesToFetch) {
		this.maxPhrasesToFetch = maxPhrasesToFetch;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public List<String> getExcludeAfterList() {
		return excludeAfterList;
	}

	public void setExcludeAfterList(List<String> excludeAfterList) {
		this.excludeAfterList = excludeAfterList;
	}

	public int getUrlMaxLength() {
		return urlMaxLength;
	}

	public void setUrlMaxLength(int urlMaxLength) {
		this.urlMaxLength = urlMaxLength;
	}

	public List<String> getUrlMustNotContain() {
		return urlMustNotContain;
	}

	public void setUrlMustNotContain(List<String> urlMustNotContain) {
		this.urlMustNotContain = urlMustNotContain;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getDatasourcePath() {
		return datasourcePath;
	}

	public void setDatasourcePath(String datasourcePath) {
		this.datasourcePath = datasourcePath;
	}

	public String getIndexDir() {
		return indexDir;
	}

	public void setIndexDir(String indexPath) {
		this.indexDir = indexPath;
	}

	public String[] getPhraseMustNotStartWith() {
		return phraseMustNotStartWith;
	}

	public String getTxtOutputFile() {
		return txtOutputFile;
	}

    public Long getNbPhraseLog() {
        return nbPhraseLog;
    }

    public void setNbPhraseLog(Long nbPhraseLog) {
        this.nbPhraseLog = nbPhraseLog;
    }
}
