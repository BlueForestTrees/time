package wiki.component.util;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wiki.entity.Page;

@Component
public class PageMemRepo {

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	
	@Autowired
	private int urlMaxLength;
	
	public boolean thisPageUrlIsnotTooLong(Page page) {
		return page.getUrl().length() <= urlMaxLength;
	}

	public void rememberThisPage(Page page) {
		urlsLowerCase.add(page.getUrl().toLowerCase());
	}

	public boolean isThisPageNew(Page page) {
		return !urlsLowerCase.contains(page.getUrl().toLowerCase());
	}
}
