package time.transformer.component.filter;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.repo.bean.Page;

@Component
public class PageFilter {

    private HashSet<String> urlsLowerCase = new HashSet<String>();
    private String[] urlBlackList = new String[] { "Discussion_utilisateur:","Discussion_utilisatrice:","Utilisatrice:", "#", "Portail:", "Discussion_modèle:", "Discussion_mod%C3%A8le:", "Discussion:" };

    @Autowired
    private int urlMaxLength;

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

        final boolean urlBlackListed = Arrays.stream(urlBlackList).anyMatch(term -> url.contains(term));
        final boolean urlTooLong = page.getUrl().length() > urlMaxLength;

        return !urlTooLong && !urlBlackListed;
    }

    public boolean isValidNewPage(Page page) {
        return isNewPage(page) && isValidPage(page);
    }

}
