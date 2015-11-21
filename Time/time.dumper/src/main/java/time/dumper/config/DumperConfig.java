package time.dumper.config;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * Regroupe l'ensemble du paramétrage d'un dump.
 * @author Slimane
 *
 */
@Component
public class DumperConfig {
    
    @Autowired
    private ApplicationArguments args;
    
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

	public DumperConfig(){
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

	public String getConfAsString(){
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append("\n----------CRAWL CONFIG-------------");
		sb.append("\nseedUrl="+getSeedUrl());
		sb.append("\nnbCrawlers="+getNbCrawlers());
		sb.append("\nmaxPages="+getMaxPages());
		sb.append("\ndelay="+getDelay());
		sb.append("\ncrawlPath="+getCrawlPath());
		sb.append("\nbaseUrl="+getBaseUrl());
		sb.append("\nnbPageLog="+getNbPageLog());
		sb.append("\nresumable="+isResumable());
		sb.append("\nwrite="+isWrite());
		sb.append("\nstoragePath="+getStoragePath());
		sb.append("\nmaxFileSize="+getMaxFileSize());
		sb.append("\n--------END CRAWL CONFIG-----------");
		sb.append("\n");
		
		return sb.toString();
	}

}
