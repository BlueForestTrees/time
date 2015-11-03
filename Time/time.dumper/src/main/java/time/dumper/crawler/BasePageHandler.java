package time.dumper.crawler;

import java.util.Arrays;
import java.util.regex.Pattern;

import time.dumper.writer.IWriter;
import time.tool.chrono.Chrono;

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
	private String[] toExclude = new String[] { "spécial:", "sp%c3%a9cial:", "discussion_wikipédia:", "discussion_wikip%c3%a9dia:", "cat%c3%a9gorie:", "catégorie:", "utilisateur:", "projet:", "discussion_projet:", "aide:", "wikipédia:", "wikip%c3%a9dia:", "fichier:" };

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
		final String href = url.toLowerCase();
		if (href.startsWith(baseUrl) && !filters.matcher(href).matches() && !Arrays.stream(toExclude).anyMatch(term -> href.contains(term))) {
			return true;
		}
		return false;
	}

}