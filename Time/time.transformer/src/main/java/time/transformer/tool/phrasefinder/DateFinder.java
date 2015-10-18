package time.transformer.tool.phrasefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import time.repo.bean.Phrase;
import time.transformer.tool.parser.IParser;

public class DateFinder {

	private final Pattern pattern;
	private final IParser parser;
	
	public DateFinder(Pattern pattern, IParser parser) {
		this.pattern = pattern;
		this.parser = parser;
	}

	/**
	 * Renvoie une {@link List<Phrase>} de Phrase detectées.
	 * @param phrases
	 * @return
	 */
	public List<Phrase> findPhrasesWithDates(String[] phrases) {
		final List<Phrase> result = new ArrayList<>();
		for (String phraseString : phrases) {
			final Matcher matcher = pattern.matcher(phraseString);
			while (matcher.find()) {
				final String dateExtract = matcher.group();
				final String number = matcher.group("g");
				final Long date = parser.from(number);
				final Phrase phrase = new Phrase();
				phrase.setText(phraseString.replace(dateExtract, "<strong>"+dateExtract+"</strong>"));
				phrase.setDate(date);
				phrase.setDateByTen(date/10L);
				phrase.setDateByTen3(date/10000L);
				phrase.setDateByTen6(date/10000000L);
				phrase.setDateByTen9(date/10000000000L);
				result.add(phrase);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return parser.getClass().getName();
	}
	
	

}
