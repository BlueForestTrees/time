package time.downloader;

import java.io.IOException;
import java.util.regex.Pattern;

public class Config extends BaseConfig {

	public Config(String basePath, String name) throws IOException {
		super(basePath, name);
	}

	public String getSep() {
		return asstring(ConfigKeys.sep);
	}

	public Pattern getFilter() {
		return Pattern.compile(asstring(ConfigKeys.filter));
	}

	public String getBaseUrl() {
		return asstring(ConfigKeys.baseUrl);
	}

	public long getNbPageLog() {
		return aslong(ConfigKeys.nbPageLog);
	}

	public int getMaxPages() {
		return asint(ConfigKeys.maxPages);
	}

	public int getDelay() {
		return asint(ConfigKeys.politenessDelay);
	}

	public String getCrawlStorageFolder() {
		return asstring(ConfigKeys.crawlStorageFolder);
	}

	public boolean isResumable() {
		return asbool(ConfigKeys.resumable);
	}

	public String getSeedUrl() {
		return asstring(ConfigKeys.seedUrl);
	}

	public boolean isHelp() {
		return asbool(ConfigKeys.help);
	}

	public int getNbCrawlers() {
		return asint(ConfigKeys.nbCrawlers);
	}

	public String getStoragePath() {
		return asstring(ConfigKeys.storagePath);
	}

	public String getMaxFileSize() {
		return asstring(ConfigKeys.maxFileSize);
	}

}
