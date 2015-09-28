package wiki.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.repo.bean.Datation;
import wiki.tool.parser.AnneeParser;
import wiki.tool.parser.IParser;
import wiki.tool.parser.JCParser;
import wiki.tool.parser.MilliardParser;
import wiki.tool.parser.MillionParser;
import wiki.tool.parser.RomanParser;
import wiki.tool.phrasefinder.DateFinder;

@Configuration
public class DateFinderConfig {

	@Bean
	public DateFinder milliardFinder(){
		final Pattern pattern = Pattern.compile("(il y a( environ)? (?<g>\\d{1,}([,\\.]\\d{1,})?) milliards d'années)");
		final IParser parser = new MilliardParser();
		final Datation datation = Datation.MILLIARD;
		return new DateFinder(pattern, parser, datation);
	}
	
	@Bean
	public DateFinder millionFinder(){
		final Pattern pattern = Pattern.compile("(il y a( environ)? (?<g>\\d{1,}([,\\.]\\d{1,})?) millions d'années)");
		final IParser parser = new MillionParser();
		final Datation datation = Datation.MILLION;
		return new DateFinder(pattern, parser, datation);
	}
	@Bean
	public DateFinder jcFinder(){
		final Pattern pattern = Pattern.compile("(vers|environ) ((?<g>\\d{4})( ans)? (avant|av.) J.-C.)");
		final IParser parser = new JCParser();
		final Datation datation = Datation.JC;
		return new DateFinder(pattern, parser, datation);
	}
	@Bean
	public DateFinder romanFinder(){
		final Pattern pattern = Pattern.compile("( (?<g>[ixvlcdmIXVLCDM]+)e siècle)");
		final IParser parser = new RomanParser();
		final Datation datation = Datation.ROMAN;
		return new DateFinder(pattern, parser, datation);
	}
	@Bean
	public DateFinder enanneFinder(){
		final Pattern pattern = Pattern.compile("([Ee]n (?<g>(-)?\\d{4}))");
		final IParser parser = new AnneeParser();
		final Datation datation = Datation.ENANNEE;
		return new DateFinder(pattern, parser, datation);
	}
	@Bean
	public DateFinder annee2DotFinder(){
		final Pattern pattern = Pattern.compile("((?<g>(-)?\\d{4}) :)");
		final IParser parser = new AnneeParser();
		final Datation datation = Datation.ANNEE2DOT;
		return new DateFinder(pattern, parser, datation);
	}
}
