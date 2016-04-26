package time.conf;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Conf {
	public static final String sep = "\\|¨";
	
	private String target;
	private String home;
	private String splitParagraphPattern = "[\r\n\t]+";
	private String basePath;
	private String name;
	private String baseUrl;
	private String regexUrlFilter;
	private String urlFilter;
	private String storagePath;
	private Long nbPageLog = 1000L;
	private int maxPages = -1;
	private int politenessDelay = 25;
	private String crawlStorageFolder;
	private boolean resumable = false;
	private String seedUrl;
	private int nbCrawlers = 5;
	private long pageSize = 100;	
	private String maxFileSize = "100MB";
	private String type;
	private List<String> excludeList;
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
	private long pageTotal;
	private long maxPhrasesToFetch;
	private int maxLength = 1000;
	private int minLength = 40;
	private List<String> excludeAfterList;
	private int urlMaxLength = 255;
	private List<String> urlBlackList = Arrays.asList();
	private String sourcePath;
	private String datasourcePath;
	private String indexDir;
	private String[] phraseMustNotStartWith;
	private String notInDateWords;

	
	public String getHome() {
		return withSlash(home);
	}
	
	public String getTarget() {
		return withSlash(target);
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
	
	public Pattern getFilter() {
		return Pattern.compile(urlFilter);
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

	public int getDelay() {
		return politenessDelay;
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

	public String getMaxFileSize() {
		return maxFileSize;
	}
	
	private String withSlash(final String path) {
		return path + (!path.endsWith("/")?"/":"");
	}

	public String getType() {
		return type;
	}

	public List<String> getExcludeList() {
		return excludeList;
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
	
	public String getRegexUrlFilter() {
		return regexUrlFilter;
	}

	public String getIncludePattern() {
		return includePattern;
	}
	
	public String getExcludePattern() {
		return excludePattern;
	}
	
	public static String getSep() {
		return sep;
	}

	public String getBasePath() {
		return basePath;
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

	public long getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(long pageTotal) {
		this.pageTotal = pageTotal;
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

	public List<String> getUrlBlackList() {
		return urlBlackList;
	}

	public void setUrlBlackList(List<String> urlBlackList) {
		this.urlBlackList = urlBlackList;
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

	public String getSplitParagraphPattern() {
		return splitParagraphPattern;
	}

	public void setSplitParagraphPattern(String splitParagraphPattern) {
		this.splitParagraphPattern = splitParagraphPattern;
	}

	public String[] getPhraseMustNotStartWith() {
		return phraseMustNotStartWith;
	}

	public String getTxtOutputFile() {
		return txtOutputFile;
	}
}
