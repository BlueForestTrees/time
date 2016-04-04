package time.transformer.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.transformer.phrase.finder.PhraseFinder;
import time.transformer.phrase.finder.parser.AnneeParser;
import time.transformer.phrase.finder.parser.ExcludingJCParser;
import time.transformer.phrase.finder.parser.IParser;
import time.transformer.phrase.finder.parser.IlYAParser;
import time.transformer.phrase.finder.parser.JCParser;
import time.transformer.phrase.finder.parser.MilliardParser;
import time.transformer.phrase.finder.parser.PreciseParser;
import time.transformer.phrase.finder.parser.RomanParser;

@Configuration
public class DateFinderConfig {
    
	private static final String START = "(^| |,|;)";
    private final static String JOUR = "((?<d>\\d{1,2})(er)? )?";
    private final static String MOIS = "(?<m>(J|j)an(\\.|v\\.|vier)|(F|f)(é|e)v(\\.|rier)|(M|m)ar(\\.|s)|(A|a)vr(\\.|il)|(M|m)ai|(J|j)uin|(J|j)uil(\\.|let)|(A|a)o(u|û)(\\.|t)|(S|s)ep(\\.|t\\.|tembre)|(O|o)ct(\\.|obre)|(N|n)ov(\\.|embre)|(D|d)(é|e)c(\\.|embre))";
    private final static String ANNEE = " (?<y>\\d{3,4})";
	private final static String REF = "(?<neg> (avant|av.) (J.-?C.|notre ère|le présent))";
	public final static String TEXT_NUMBERS = "un|deux|trois|quatre|cinq|six|sept|huit|neuf|dix|onze|douze|treize|quatorze|quinze|seize|dix-sept|dix-huit|dix-neuf|vingt";
    private final static String TEXT_NUMBERS_ = "(?<gt>(" + TEXT_NUMBERS + "))"; 
	private final static String NUMBERS = "(?<g>\\d+([,\\.]\\d+)?)";
	private static final String ILYAENVIRON = "([Ii]l y a|[Vv]oici|(datent|vie(ux|ille)) d(e)?|au cours des|dès)([ ']environ)?( quelque)?( près de)?";
	private static final String EXCLUDEDS = "(?<ex>(degré|coup|pa|tour|heure|minute|seconde|mois)s?)?";
		
    @Bean
    public PhraseFinder ilYAFinder(){
    	final Pattern pattern = Pattern.compile("(?<neg>"+ILYAENVIRON+") (?<g>\\d{1,3}( ?000)?) (ans|dernières années)");
        final IParser parser = new IlYAParser();
        return new PhraseFinder(pattern, parser, "ilYAFinder");
    }
    
    @Bean
    public PhraseFinder milliardFinder() {
        final Pattern pattern = Pattern.compile("("+NUMBERS+"|"+TEXT_NUMBERS_+") milli(?<s>ard|on)s? d['’]années");
        final IParser parser = new MilliardParser();
        return new PhraseFinder(pattern, parser, "milliardFinder");
    }

    @Bean
    public PhraseFinder jcFinder() {
        final Pattern pattern = Pattern.compile(START + "([Aà] partir de|date de|depuis|[Ee]n|dans les années) (l'an )?(?<g>(-)?\\d{2,9})"+REF+"?(;|,|\\.| "+EXCLUDEDS+"|$)");
        final IParser parser = new ExcludingJCParser();
        return new PhraseFinder(pattern, parser, "jcFinder");
    }
    
    @Bean
    public PhraseFinder nearJcFinder() {
		final Pattern pattern = Pattern.compile(START+"([Ee]nviron) (?<g>\\d{2,4})(,? )ans"+REF);
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearJcFinder");
    }
    
    @Bean
    public PhraseFinder nearJcFinder2() {
        final Pattern pattern = Pattern.compile(START+"([Vv]ers l'an|après) (?<g>(-)?\\d{2,4})(,?)"+REF+"?");
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearJcFinder2");
    }
    
    @Bean
    public PhraseFinder nearJcFinder3() {
        final Pattern pattern = Pattern.compile(START+"[Vv]ers (?<g>\\d{2,4})"+REF);
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearJcFinder3");
    }
    
    @Bean
    public PhraseFinder nearLessFinder() {
        final Pattern pattern = Pattern.compile(START + "([Ee]nviron) (?<neg>-)(?<g>\\d{2,4})");
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearLessFinder");
    }

    @Bean
    public PhraseFinder romanFinder() {
        final Pattern pattern = Pattern.compile(" (?<g>[ixvIXV]+)e siècle"+REF+"?");
        final IParser parser = new RomanParser();
        return new PhraseFinder(pattern, parser, "romanFinder");
    }

    @Bean
    public PhraseFinder annee2DotFinder() {
        final Pattern pattern = Pattern.compile("^(([Vv]ers|[Ee]nviron|[Ee]n) )?(?<g>([ -])?\\d{4}) ?:");
        final IParser parser = new AnneeParser();
        return new PhraseFinder(pattern, parser, "annee2DotFinder");
    }

    @Bean
    public PhraseFinder preciseFinder() {
        final Pattern pattern = Pattern.compile("(?<g>" + JOUR +  MOIS + ANNEE + ")" + REF+"?");
        final IParser parser = new PreciseParser();
        return new PhraseFinder(pattern, parser , "preciseFinder");
    }

    @Bean
    public PhraseFinder[] finders() {
        return new PhraseFinder[] { milliardFinder(), jcFinder(),nearJcFinder(),ilYAFinder(), nearLessFinder(), nearJcFinder2(), nearJcFinder3(), romanFinder(), annee2DotFinder(), preciseFinder() };
    }
}
