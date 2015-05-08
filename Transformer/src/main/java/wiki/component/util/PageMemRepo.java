package wiki.component.util;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wiki.entity.Page;
import wiki.entity.Phrase;

@Component
public class PageMemRepo {

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	
	@Autowired
	private int urlMaxLength;
	
	public boolean thisPageUrlIsnotTooLong(Page page) {
		return page.getUrl().length() <= urlMaxLength;
	}

	public void rememberThisPage(Page page) {
		urlsLowerCase.add(page.getUrl().toLowerCase().replace("-", "_"));
	}

	public boolean isThisPageNew(Page page) {
		return !urlsLowerCase.contains(page.getUrl().toLowerCase());
	}
	
	public boolean keepThisPhrase(Phrase phrase){
		boolean startWithCatogories = phrase.getText().startsWith("Catégories :");
		boolean tooLong = phrase.getText().length() > 1000;
		return !tooLong && !startWithCatogories;
	}
}
