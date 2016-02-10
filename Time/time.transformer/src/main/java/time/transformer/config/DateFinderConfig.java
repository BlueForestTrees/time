package time.transformer.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.transformer.phrase.finder.PhraseFinder;
import time.transformer.phrase.finder.parser.AnneeParser;
import time.transformer.phrase.finder.parser.IParser;
import time.transformer.phrase.finder.parser.IlYAParser;
import time.transformer.phrase.finder.parser.JCParser;
import time.transformer.phrase.finder.parser.MilliardParser;
import time.transformer.phrase.finder.parser.PreciseParser;
import time.transformer.phrase.finder.parser.RomanParser;

@Configuration
public class DateFinderConfig {
    
    private final static String JOUR = "(?<d>\\d{1,2})";
    private final static String MOIS = "(?<m>(J|j)an(\\.|v\\.|vier)|(F|f)(é|e)v(\\.|rier)|(M|m)ar(\\.|s)|(A|a)vr(\\.|il)|(M|m)ai|(J|j)uin|(J|j)uil(\\.|let)|(A|a)o(u|û)(\\.|t)|(S|s)ep(\\.|t\\.|tembre)|(O|o)ct(\\.|obre)|(N|n)ov(\\.|embre)|(D|d)(é|e)c(\\.|embre))";
    private final static String ANNEE = "(?<y>\\d{3,4}))";
    
    @Bean
    public PhraseFinder milliardFinder() {
        final Pattern pattern = Pattern.compile("il y a( environ)? (?<g>\\d+([,\\.]\\d+)?) milli(?<s>ard|on)s? d'années");
        final IParser parser = new MilliardParser();
        return new PhraseFinder(pattern, parser, "milliardFinder");
    }

    @Bean
    public PhraseFinder jcFinder() {
        final Pattern pattern = Pattern.compile("(^| |,)([Aà] partir de|[Ee]n) (l'an )?(?<g>(-)?\\d{2,4}),? (?<neg>(avant|av.) (J.-?C.|notre ère))?");
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "jcFinder");
    }
    
    @Bean
    public PhraseFinder nearJcFinder() {
        final Pattern pattern = Pattern.compile("(^| |,)([Ee]nviron) (?<g>\\d{2,4})(,? )ans(?<neg> (avant|av.) (J.-?C.|notre ère))");
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearJcFinder");
    }
    
    @Bean
    public PhraseFinder nearJcFinder2() {
        final Pattern pattern = Pattern.compile("(^| |,)([Vv]ers l'an) (?<g>(-)?\\d{2,4})(,?)(?<neg> (avant|av.) (J.-?C.|notre ère))?");
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearJcFinder2");
    }
    
    @Bean
    public PhraseFinder nearJcFinder3() {
        final Pattern pattern = Pattern.compile("(^| |,)[Vv]ers (?<g>\\d{2,4})(?<neg> (avant|av.) (J.-?C.|notre ère))");
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearJcFinder3");
    }
    
    @Bean
    public PhraseFinder nearLessFinder() {
        final Pattern pattern = Pattern.compile("(^| |,)([Ee]nviron) (?<neg>-)(?<g>\\d{2,4})");
        final IParser parser = new JCParser();
        return new PhraseFinder(pattern, parser, "nearLessFinder");
    }
    
    @Bean
    public PhraseFinder ilYAFinder() {
        final Pattern pattern = Pattern.compile("(^| |,)(?<neg>il y a environ) (?<g>\\d{2,4}) ans");
        final IParser parser = new IlYAParser();
        return new PhraseFinder(pattern, parser, "ilYAFinder");
    }

    @Bean
    public PhraseFinder romanFinder() {
        final Pattern pattern = Pattern.compile(" (?<g>[ixvIXV]+)e siècle");
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
        final Pattern pattern = Pattern.compile("(^| |,)(?<g>(" + JOUR + "|(e|E)n|[Aà] partir de) "+ MOIS + " " + ANNEE + "(?<neg> (avant|av.) (J.-?C.|notre ère))?");
        final IParser parser = new PreciseParser();
        return new PhraseFinder(pattern, parser , "preciseFinder");
    }

    @Bean
    public PhraseFinder[] finders() {
        return new PhraseFinder[] { milliardFinder(), jcFinder(),nearJcFinder(),ilYAFinder(), nearLessFinder(), nearJcFinder2(), nearJcFinder3(), romanFinder(), annee2DotFinder(), preciseFinder() };
    }
}
