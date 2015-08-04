package wiki.tool.phrasefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wiki.entity.Datation;
import wiki.entity.Phrase;
import wiki.tool.parser.IParser;

public class DateFinder {

	private final Pattern pattern;
	private final IParser parser;
	private final Datation datation;
	
	public DateFinder(Pattern pattern, IParser parser, Datation datation) {
		this.pattern = pattern;
		this.parser = parser;
		this.datation = datation;
	}

	public List<Phrase> findPhrasesWithDates(String[] phrases) {
		final List<Phrase> result = new ArrayList<>();
		for (String phraseString : phrases) {
			final Matcher matcher = pattern.matcher(phraseString);
			while (matcher.find()) {
				//TODO tester le strong sur la date
				final String dateExtract = matcher.group();
				final String number = matcher.group("g");
				final Long date = parser.from(number);
				final Phrase phrase = new Phrase();
				phrase.setText(phraseString.replace(dateExtract, "<strong>"+dateExtract+"</strong>"));
				phrase.setType(datation);
				phrase.setDate(date);
				result.add(phrase);
			}
		}
		return result;
	}

}
