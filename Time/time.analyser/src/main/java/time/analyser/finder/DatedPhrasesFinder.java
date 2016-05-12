package time.analyser.finder;

import time.analyser.finder.parser.IParser;
import time.domain.DatedPhrase;
import time.tool.string.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatedPhrasesFinder {

	private final Pattern pattern;
	private final IParser parser;
	private final Finder name;

	public DatedPhrasesFinder(Pattern pattern, IParser parser, Finder name) {
		this.pattern = pattern;
		this.parser = parser;
		this.name = name;
	}

	public List<DatedPhrase> findPhrases(final String phrase) {
		final List<DatedPhrase> result = new ArrayList<>();
		final Matcher matcher = pattern.matcher(phrase);
		while (matcher.find()) {
			final String dateExtract = matcher.group();
			final Long date = parser.from(matcher);
			if (date != null) {
				final DatedPhrase datedPhrase = new DatedPhrase();
				datedPhrase.setText(Strings.bold(Strings.clean(phrase), dateExtract));
				datedPhrase.setDate(date);
				result.add(datedPhrase);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return name.name();
	}

}
