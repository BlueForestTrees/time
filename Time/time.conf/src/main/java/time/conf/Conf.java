package time.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import time.tool.string.Strings;

import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conf {
	private String webRoot;
	private String target;
	private String home;
	private String homeDir;
	private Integer port;
	private String basePath;
	private String name;
	private String baseUrl;
	private String regexUrlFilter;
	private String urlFilter;
	private String storagePath;
	private Boolean resumable;
	private String seedUrl;
	private String crawlStorageFolder;
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
	private Long chronoPageTotal;
	private Long maxPhrasesToFetch;
	private List<String> excludeAfterList;
	private String sourcePath;
	private String datasourcePath;
	private String indexDir;
	private String finalIndexDir;
	private String[] phraseMustNotStartWith;
	private String mergeableIndexesDir;
	private String mergedIndexDir;
	private String baseAppendIndexDir;
	private String maxFileSize;
	private Long nbPageLog;
    private Long nbPhraseLog;
	private Long pageSize;
	private Integer nbCrawlers;
	private Integer maxPages;
	private Integer politenessDelay;
	private Integer maxLength;
	private Integer minLength;
	private Integer urlMaxLength;
	private List<String> urlMustNotContain;

	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}

	public String getWebRoot() {
		return webRoot;
	}

	public String getTarget() {
		return target;
	}

	public String getHome() {
		return home;
	}

	public String getHomeDir() {
		return homeDir;
	}

	public Integer getPort() {
		return port;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getName() {
		return name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getRegexUrlFilter() {
		return regexUrlFilter;
	}

	public String getUrlFilter() {
		return urlFilter;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public Boolean isResumable() {
		return resumable;
	}

	public String getSeedUrl() {
		return seedUrl;
	}

	public String getCrawlStorageFolder() {
		return crawlStorageFolder;
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

	public String getExcludePattern() {
		return excludePattern;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public String getTxtOutputFile() {
		return txtOutputFile;
	}

	public String getTxtOutputDir() {
		return txtOutputDir;
	}

	public String getCrawlStorageDir() {
		return crawlStorageDir;
	}

	public Long getChronoPageTotal() {
		return chronoPageTotal;
	}

	public Long getMaxPhrasesToFetch() {
		return maxPhrasesToFetch;
	}

	public List<String> getExcludeAfterList() {
		return excludeAfterList;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public String getDatasourcePath() {
		return datasourcePath;
	}

	public String getIndexDir() {
		return indexDir;
	}

	public String getFinalIndexDir() {
		return finalIndexDir;
	}

	public String[] getPhraseMustNotStartWith() {
		return phraseMustNotStartWith;
	}

	public String getMergeableIndexesDir() {
		return mergeableIndexesDir;
	}

	public String getMergedIndexDir() {
		return mergedIndexDir;
	}

	public String getBaseAppendIndexDir() {
		return baseAppendIndexDir;
	}

	public String getMaxFileSize() {
		return maxFileSize;
	}

	public Long getNbPageLog() {
		return nbPageLog;
	}

	public Long getNbPhraseLog() {
		return nbPhraseLog;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public Integer getNbCrawlers() {
		return nbCrawlers;
	}

	public Integer getMaxPages() {
		return maxPages;
	}

	public Integer getPolitenessDelay() {
		return politenessDelay;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public Integer getUrlMaxLength() {
		return urlMaxLength;
	}

	public List<String> getUrlMustNotContain() {
		return urlMustNotContain;
	}
}
