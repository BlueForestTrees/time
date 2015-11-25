package time.transformer.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.transformer.tool.parser.AnneeParser;
import time.transformer.tool.parser.IParser;
import time.transformer.tool.parser.JCParser;
import time.transformer.tool.parser.MilliardParser;
import time.transformer.tool.parser.MillionParser;
import time.transformer.tool.parser.RomanParser;
import time.transformer.tool.phrasefinder.DateFinder;

@Configuration
public class DateFinderConfig {

    @Bean
    public DateFinder milliardFinder() {
        final Pattern pattern = Pattern.compile("(il y a( environ)? (?<g>\\d{1,}([,\\.]\\d{1,})?) milliards d'années)");
        final IParser parser = new MilliardParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder millionFinder() {
        final Pattern pattern = Pattern.compile("(il y a( environ)? (?<g>\\d{1,}([,\\.]\\d{1,})?) millions d'années)");
        final IParser parser = new MillionParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder jcFinder() {
        final Pattern pattern = Pattern.compile("([Vv]ers|[Ee]nviron|[Ee]n) (?<g>(-)?\\d{4})( ans)?(?<neg> avant| av. J.-C.)?");
        final IParser parser = new JCParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder romanFinder() {
        final Pattern pattern = Pattern.compile("( (?<g>[ixvIXV]+)e siècle)");
        final IParser parser = new RomanParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder annee2DotFinder() {
        final Pattern pattern = Pattern.compile("(^(?<g>([ -])?\\d{4}) :)");
        final IParser parser = new AnneeParser();
        return new DateFinder(pattern, parser);
    }

    @Bean
    public DateFinder[] finders() {
        return new DateFinder[] { milliardFinder(), millionFinder(), jcFinder(), romanFinder(), annee2DotFinder() };
    }
}
