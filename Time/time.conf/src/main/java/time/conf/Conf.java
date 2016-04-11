package time.conf;

import java.util.regex.Pattern;

public class Conf extends BaseConf {
	
	private static final String TXTPAGES = "pages";
	private static final String TXTPAGESDIR = TXTPAGES + "/";
	private static final String SOURCES = "sources/";
	private static final String TEMP = "temp/";
	private static final String INDEXES = "indexes/";
	private static final String CONF = ".conf";
	private static final String CRAWLDIR = "crawldata";
	
	public String getHome() {
		return withSlash(asstring(ConfKeys.home));
	}
	
	public String getTarget() {
		return withSlash(asstring(ConfKeys.target));
	}
	
	public String getSourceDir(){
		return getHome() + SOURCES + getTarget();
	}
	
	public String getTempDir(){
		return getHome() + TEMP + getTarget();
	}
	
	public String getIndexDir(){
		return getHome() + INDEXES + getTarget();		
	}
	
	public String getTxtPagesDir() {
		return getHome() + TXTPAGESDIR + getTarget();
	}
	public String getPagesFile1() {
		return getTxtPagesDir() + TXTPAGES;
	}
	public String getCrawlStorageDir() {
		return withSlash(getTempDir())+CRAWLDIR;
	}
	
	public String getConfFile(){
		return getSourceDir() + CONF;
	}
	
	public Pattern getFilter() {
		return Pattern.compile(asstring(ConfKeys.urlFilter));
	}

	public String getBaseUrl() {
		return asstring(ConfKeys.baseUrl);
	}

	public long getNbPageLog() {
		return aslong(ConfKeys.nbPageLog, 1000);
	}

	public int getMaxPages() {
		return asint(ConfKeys.maxPages, -1);
	}

	public int getDelay() {
		return asint(ConfKeys.politenessDelay, 25);
	}

	public boolean isResumable() {
		return asbool(ConfKeys.resumable, false);
	}

	public String getSeedUrl() {
		return asstring(ConfKeys.seedUrl);
	}

	public int getNbCrawlers() {
		return asint(ConfKeys.nbCrawlers, 5);
	}

	public String getMaxFileSize() {
		return asstring(ConfKeys.maxFileSize, "100MB");
	}
	
	private String withSlash(final String path) {
		return path + (!path.endsWith("/")?"/":"");
	}

	public String getType() {
		return asstring(ConfKeys.type);
	}

	public String[] getUrlBlackList() {
		return asstringarray(ConfKeys.urlBlackList);
	}
	
	public String[] getContentExclusion() {
		return asstringarray(ConfKeys.contentExclusion);
	}

	public String getSource() {
		return asstring(ConfKeys.source);
	}

	public String getUrl() {
		return asstring(ConfKeys.url);
	}

	public String getTitle() {
		return asstring(ConfKeys.title);
	}
	
	public String getRegexUrlFilter() {
		return asstring(ConfKeys.regexUrlFilter);
	}
	
}
