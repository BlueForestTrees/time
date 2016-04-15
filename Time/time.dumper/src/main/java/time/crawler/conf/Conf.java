package time.crawler.conf;

import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Conf {
	private static final String TXTPAGES = "pages";
	private static final String TEMP = "temp/";
	private static final String INDEXES = "indexes/";
	private static final String CONF = ".conf";
	public static final String sep = "|¨";
	
	private String target;
	private String home;
	
	private String basePath;
	private String name;
	private String baseUrl;
	private String regexUrlFilter;
	private String urlFilter;
	@JsonProperty(defaultValue="1000")
	private Long nbPageLog;
	@JsonProperty(defaultValue="-1")
	private int maxPages;
	@JsonProperty(defaultValue="25")
	private int politenessDelay;
	private String crawlStorageFolder;
	@JsonProperty(defaultValue="false")
	private boolean resumable;
	private String seedUrl;
	@JsonProperty(defaultValue="5")
	private int nbCrawlers;
	private String storagePath;
	
	@JsonProperty(defaultValue="100MB")
	private String maxFileSize;
	private String type;
	private List<String> excludeList;
	private List<String> contentExclusion;
	private String source;
	private String url;
	private String title;
	private String includePattern;
	private String excludePattern;
	@JsonProperty
	private String sourceDir;
	@JsonProperty
	private String txtPagesDir;
	@JsonProperty
	private String crawlStorageDir;
	
	public String getHome() {
		return withSlash(home);
	}
	
	public String getTarget() {
		return withSlash(target);
	}
	
	public String getSourceDir(){
		return sourceDir;
	}
	
	public String getTempDir(){
		return getHome() + TEMP + getTarget();
	}
	
	public String getIndexDir(){
		return getHome() + INDEXES + getTarget();		
	}
	
	public String getTxtPagesDir() {
		return txtPagesDir;
	}
	public String getPagesFile1() {
		return getTxtPagesDir() + TXTPAGES;
	}
	public String getCrawlStorageDir() {
		return crawlStorageDir;
	}
	
	public String getConfFile(){
		return getSourceDir() + CONF;
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
}
