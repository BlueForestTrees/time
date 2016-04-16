package time.transformer.page.filter;

import java.util.HashSet;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.Page;

public class PageFilter {

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	private int urlMaxLength;
	private List<String> urlBlackList;

	@Inject
	public PageFilter(@Named("conf") Conf conf) {
		this.urlBlackList = conf.getUrlBlackList();
		this.urlMaxLength = conf.getUrlMaxLength();
	}

	public void rememberThisPage(Page page) {
		urlsLowerCase.add(normalizedUrl(page));
	}

	public String normalizedUrl(Page page) {
		return page.getUrl().toLowerCase().replace("-", "_");
	}

	public boolean isNewPage(Page page) {
		return !urlsLowerCase.contains(normalizedUrl(page));
	}

	public boolean isValidPage(Page page) {
		final String url = page.getUrl();
		final boolean urlBlackListed = urlBlackList.stream().anyMatch(term -> url.contains(term));
		final boolean urlTooLong = page.getUrl().length() > urlMaxLength;

		return !urlTooLong && !urlBlackListed;
	}

	public boolean isValidNewPage(Page page) {
		return isNewPage(page) && isValidPage(page);
	}

}
