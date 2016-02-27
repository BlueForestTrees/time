package time.transformer.page.transformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import time.repo.bean.Page;

/**
 * Supprime le contenu de la page situé après les mots clés {@link excludeAfter}
 * @author slim
 *
 */
public class WikiUrlDatePageTransformer implements IPageTransformer {

    //TODO si https://fr.wikipedia.org/wiki/141_av._J.-C. => années négatives (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/Ann%C3%A9es_100 => décennies (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/IIe_si%C3%A8cle => siècle (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/Ier_mill%C3%A9naire => millenaire (_av._J.-C. négative)
    private final Pattern yearPattern = Pattern.compile("/[0-9]+(?<neg>_av._J.-C.)?");
    private final Pattern decadePattern = Pattern.compile("");
    private final Pattern centuryPattern = Pattern.compile("");
    private final Pattern milleniumPattern = Pattern.compile("");
    
    private final IPageTransformer yearTransformer = null;
    private final IPageTransformer decadeTransformer = null;
    private final IPageTransformer centuryTransformer = null;
    private final IPageTransformer milleniumTransformer = null;
    
    @Override
    public Page transform(final Page page) {
        final IPageTransformer transformer = getAvailableTransformer(page.getUrl());
        if(transformer != null){
            return transformer.transform(page);
        }else{
            return page;
        }
    }
    
    private IPageTransformer getAvailableTransformer(final String url){
        final Matcher yearMatcher = yearPattern.matcher(url);
        if(yearMatcher.matches()){
            return yearTransformer;
        }
        final Matcher decadeMatcher = decadePattern.matcher(url);
        if(decadeMatcher.matches()){
            return decadeTransformer;
        }
        final Matcher centuryMatcher = centuryPattern.matcher(url);
        if(centuryMatcher.matches()){
            return centuryTransformer;
        }
        final Matcher milleniumMatcher = milleniumPattern.matcher(url);
        if(yearMatcher.matches()){
            return milleniumTransformer;
        }
        return null;
    }
    
}
