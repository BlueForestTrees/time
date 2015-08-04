package wiki.crawler;

import java.util.regex.Pattern;

import wiki.tool.chrono.Chrono;
import wiki.writer.IWriter;

public abstract class BasePageHandler implements IPageHandler {

	protected Pattern filters;
	protected long pageCount;
	protected long nbPageLog;
	protected boolean write;
	protected String baseUrl;
	protected Chrono chrono;
	protected Chrono fullChrono;
	protected long nbLog;
	protected IWriter writer;


	public Pattern getFilters() {
		return filters;
	}
	
	public void setFilters(Pattern filters) {
		this.filters = filters;
	}
	
	public IWriter getWriter() {
		return writer;
	}

	public void setWriter(IWriter writer) {
		this.writer = writer;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public long getNbPageLog() {
		return nbPageLog;
	}

	public void setNbPageLog(long nbPageLog) {
		this.nbPageLog = nbPageLog;
	}

	@Override
	public boolean shouldVisit(String url) {
		String href = url.toLowerCase();
		return !filters.matcher(href).matches() && href.startsWith(baseUrl);
	}

}