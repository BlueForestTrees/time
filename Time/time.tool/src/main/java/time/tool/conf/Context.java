package time.tool.conf;

import java.util.regex.Pattern;

public class Context extends BaseContext {

	public String getSep() {
		return asstring(ConfKeys.sep);
	}

	public Pattern getFilter() {
		return Pattern.compile(asstring(ConfKeys.filter));
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
		return asstring(ConfKeys.crawlStorageFolder);
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
		return asstring(ConfKeys.storagePath);
	}

	public String getMaxFileSize() {
		return asstring(ConfKeys.maxFileSize);
	}

}
