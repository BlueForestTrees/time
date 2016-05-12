package time.storage.transform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import time.domain.Text;

/**
 * Supprime le contenu de la page situé après les mots clés {@link excludeAfter}
 * @author slim
 *
 */
public class WikiUrlDateTextTransformer implements ITextTransformer {
	
	/*
	 * pattern:
	 * 24 avril :
	 * 1er juillet-18 novembre :
	 * 1er-2 décembre :
	 * 1er janvier, Cameroun :
	 * 29 - 31 décembre, Inde :
	 * 27-29 avril :
	 * 12 septembre-11 décembre :
	 * exclude Naissances et Décès
	 */

    //TODO si https://fr.wikipedia.org/wiki/141_av._J.-C. => années négatives (_av._J.-C. négative)
	//TODO si https://fr.wikipedia.org/wiki/1915_en_a%C3%A9ronautique page thématique
    //TODO si https://fr.wikipedia.org/wiki/Ann%C3%A9es_100 => décennies (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/IIe_si%C3%A8cle => siècle (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/Ier_mill%C3%A9naire => millenaire (_av._J.-C. négative)
    private final Pattern yearPattern = Pattern.compile("/[0-9]+(?<neg>_av._J.-C.)?");
    private final Pattern decadePattern = Pattern.compile("");
    private final Pattern centuryPattern = Pattern.compile("");
    private final Pattern milleniumPattern = Pattern.compile("");
    
    private final ITextTransformer yearTransformer = null;
    private final ITextTransformer decadeTransformer = null;
    private final ITextTransformer centuryTransformer = null;
    private final ITextTransformer milleniumTransformer = null;
    
    @Override
    public Text transform(final Text text) {
        final ITextTransformer transformer = getAvailableTransformer(text.getUrl());
        if(transformer != null){
            return transformer.transform(text);
        }else{
            return text;
        }
    }
    
    private ITextTransformer getAvailableTransformer(final String url){
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
        if(milleniumMatcher.matches()){
            return milleniumTransformer;
        }
        return null;
    }
    
}
