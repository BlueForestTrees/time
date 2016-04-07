package time.conf;

import java.util.regex.Pattern;

public class Conf extends BaseConf {
	
	private static final String PHRASESFOLDER = "phrases";
	private static final String SOURCES = "sources/";
	private static final String TEMP = "temp/";
	private static final String INDEXES = "indexes/";
	private static final String CONF = ".conf";
	private static final String CRAWLFOLDER = "crawldata";
	
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
	
	public String getConfFile(){
		return getSourceDir() + CONF;
	}
	
	public String getSep() {
		return asstring(ConfKeys.sep);
	}

	public Pattern getFilter() {
		return Pattern.compile(asstring(ConfKeys.urlFilter));
	}

	public String getBaseUrl() {
		return asstring(ConfKeys.baseUrl);
	}

	public long getNbPageLog() {
		return aslong(ConfKeys.nbPageLog);
	}

	public int getMaxPages() {
		return asint(ConfKeys.maxPages);
	}

	public int getDelay() {
		return asint(ConfKeys.politenessDelay);
	}

	public String getCrawlStorageFolder() {
		return withSlash(getTempDir())+CRAWLFOLDER;
	}

	public boolean isResumable() {
		return asbool(ConfKeys.resumable);
	}

	public String getSeedUrl() {
		return asstring(ConfKeys.seedUrl);
	}

	public boolean isHelp() {
		return asbool(ConfKeys.help);
	}

	public int getNbCrawlers() {
		return asint(ConfKeys.nbCrawlers);
	}

	public String getStoragePath() {
		return withSlash(getTempDir())+PHRASESFOLDER;
	}

	public String getMaxFileSize() {
		return asstring(ConfKeys.maxFileSize);
	}
	
	private String withSlash(final String path) {
		return path + (!path.endsWith("/")?"/":"");
	}

}
