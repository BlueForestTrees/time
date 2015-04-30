package wiki.tool.phrasefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wiki.entity.Page;
import wiki.entity.Phrase;
import wiki.tool.parser.EnAnneeParser;
import wiki.tool.parser.IParser;
import wiki.tool.parser.JCParser;
import wiki.tool.parser.MilliardParser;
import wiki.tool.parser.MillionParser;
import wiki.tool.parser.RomanParser;

public enum PhraseFinder {
		MILLIARD(getPattern("MILLIARD"), new MilliardParser()),
		MILLION(getPattern("MILLION"), new MillionParser()),
		JC(getPattern("JC"), new JCParser()),
		ROMAN(getPattern("ROMAN"), new RomanParser()),
		ENANNEE(getPattern("ENANNEE"), new EnAnneeParser()),
		ANNEE2DOT(getPattern("ANNEE2DOT"), new RomanParser()
	);

	private final static String group = "?<nbGroup>";
	private final Pattern pattern;
	private final IParser parser;
	
	private PhraseFinder(Pattern pattern, IParser parser) {
		this.pattern = pattern;
		this.parser = parser;
	}

	private static Pattern getPattern(String casString) {
		PhraseFinder cas = PhraseFinder.valueOf(casString);
		switch (cas) {
		case MILLIARD:
			return Pattern.compile("(il y a( environ)? ("+group+"\\d{1,}(,\\d{1,})?) milliards d'années)");
		case MILLION:
			return Pattern.compile("(il y a( environ)? ("+group+"\\d{1,}(,\\d{1,})?) millions d'années)");
		case JC:
			return Pattern.compile("(vers|environ) (("+group+"\\d{4})( ans)? (avant|av.) J.-C.)");
		case ROMAN:
			return Pattern.compile("( ("+group+"[ixvlcdmIXVLCDM]+)e siècle)");
		case ENANNEE:
			return Pattern.compile("([Ee]n ("+group+"\\d{4}))");
		case ANNEE2DOT:
			return Pattern.compile("(("+group+"\\d{4}) :)");
		}
		return null;
	}

	public List<Phrase> findPhrase(String[] phrases, Page page) {
		List<Phrase> result = new ArrayList<>();
		for (String phraseString : phrases) {
			Matcher matcher = pattern.matcher(phraseString);
			if (matcher.find()) {
				final String number = matcher.group(group);
				Long date = parser.from(number);
				Phrase phrase = new Phrase();
				phrase.setPageId(page.getId());
				phrase.setText(phraseString);
				phrase.setType(this);
				phrase.setDate(date);
				result.add(phrase);
			}
		}
		return result;
	}

}
