package time.transformer.component.filter;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.repo.bean.Page;

@Component
public class PageFilter {

	private HashSet<String> urlsLowerCase = new HashSet<String>();

	@Autowired
	private int urlMaxLength;

	public void rememberThisPage(Page page) {
		urlsLowerCase.add(normalizedUrl(page));
	}

	public String normalizedUrl(Page page) {
		return page.getUrl().toLowerCase().replace("-", "_");
	}

	public boolean isNewPage(Page page) {
		final boolean isNew = !urlsLowerCase.contains(normalizedUrl(page));

		return isNew;
	}

	public boolean isValidPage(Page page) {
		final boolean startWithPortail = page.getUrl().startsWith("Portail:");
		final boolean urlTooLong = page.getUrl().length() > urlMaxLength;

		return !urlTooLong && !startWithPortail;
	}

	public boolean isValidNewPage(Page page) {
		return isNewPage(page) && isValidPage(page);
	}

}
