package wiki.component.util;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.repo.bean.Page;
import time.repo.bean.Phrase;

@Component
public class PageMemRepo {

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	
	@Autowired
	private int urlMaxLength;
	
	public void rememberThisPage(Page page) {
		urlsLowerCase.add(normalizedUrl(page));
	}

	public String normalizedUrl(Page page){
		return page.getUrl().toLowerCase().replace("-", "_");
	}
	
	public boolean isPageValid(Page page){
		boolean startWithPortail = page.getUrl().startsWith("Portail:");
		boolean urlTooLong = page.getUrl().length() > urlMaxLength;
		boolean isNew = !urlsLowerCase.contains(normalizedUrl(page));
		
		return isNew && !urlTooLong && !startWithPortail;
	}
	
	public boolean keepThisPhrase(Phrase phrase){
		boolean startWithCatogories = phrase.getText().startsWith("Catégories :");
		//boolean tooLong = phrase.getText().length() > 1000;
		return !startWithCatogories;
	}
}
