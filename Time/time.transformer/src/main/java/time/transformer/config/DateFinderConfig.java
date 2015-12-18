package time.transformer.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.transformer.tool.parser.AnneeParser;
import time.transformer.tool.parser.IParser;
import time.transformer.tool.parser.JCParser;
import time.transformer.tool.parser.MilliardParser;
import time.transformer.tool.parser.PreciseParser;
import time.transformer.tool.parser.RomanParser;
import time.transformer.tool.phrasefinder.DateFinder;

@Configuration
public class DateFinderConfig {

    @Bean
    public DateFinder milliardFinder() {
        final Pattern pattern = Pattern.compile("il y a( environ)? (?<g>\\d+([,\\.]\\d+)?) milli(?<s>ard|on)s? d'années");
        final IParser parser = new MilliardParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder jcFinder() {
        final Pattern pattern = Pattern.compile("(^| |,)([Aà] partir de|[Vv]ers|[Ee]nviron|[Ee]n) (l'an )?(?<g>(-)?\\d{4})(,? )(ans )?(?<neg>(avant|av.) J.-?C.)?");
        final IParser parser = new JCParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder romanFinder() {
        final Pattern pattern = Pattern.compile(" (?<g>[ixvIXV]+)e siècle");
        final IParser parser = new RomanParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder annee2DotFinder() {
        final Pattern pattern = Pattern.compile("^(([Vv]ers|[Ee]nviron|[Ee]n) )?(?<g>([ -])?\\d{4}) ?:");
        final IParser parser = new AnneeParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder preciseFinder() {
        final Pattern pattern = Pattern.compile("(^| |,)(?<g>((?<d>\\d{1,2})|(e|E)n|[Aà] partir de) (?<m>(J|j)an(\\.|v\\.|vier)|(F|f)(é|e)v(\\.|rier)|(M|m)ar(\\.|s)|(A|a)vr(\\.|il)|(M|m)ai|(J|j)uin|(J|j)uil(\\.|let)|(A|a)o(u|û)(\\.|t)|(S|s)ep(\\.|t\\.|tembre)|(O|o)ct(\\.|obre)|(N|n)ov(\\.|embre)|(D|d)(é|e)c(\\.|embre)) (?<y>\\d{3,4}))");
        final IParser parser = new PreciseParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder[] finders() {
        return new DateFinder[] { milliardFinder(), jcFinder(), romanFinder(), annee2DotFinder(), preciseFinder() };
    }
}
