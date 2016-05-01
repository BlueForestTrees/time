package time.transformer.page.filter;

import java.util.HashSet;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.Text;

public class PageFilter {

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	private int urlMaxLength;
	private List<String> urlBlackList;

	@Inject
	public PageFilter(@Named("conf") Conf conf) {
		this.urlBlackList = conf.getUrlBlackList();
		this.urlMaxLength = conf.getUrlMaxLength();
	}

	public void rememberThisPage(Text text) {
		urlsLowerCase.add(normalizedUrl(text));
	}

	public String normalizedUrl(Text text) {
		return text.getUrl().toLowerCase().replace("-", "_");
	}

	public boolean isNewPage(Text text) {
		return !urlsLowerCase.contains(normalizedUrl(text));
	}

	public boolean isValidPage(Text text) {
		final String url = text.getUrl();
		final boolean urlBlackListed = urlBlackList.stream().anyMatch(term -> url.contains(term));
		final boolean urlTooLong = text.getUrl().length() > urlMaxLength;

		return !urlTooLong && !urlBlackListed;
	}

	public boolean isValidNewPage(Text text) {
		return isNewPage(text) && isValidPage(text);
	}

}
