package time.analyser;

import time.analyser.parser.IParser;
import time.domain.DatedPhrase;
import time.tool.string.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhrasesAnalyser {

	private final Pattern dateMatchPattern;
	private final IParser parser;
	private final DateType name;

	public PhrasesAnalyser(final Pattern dateMatchPattern, final IParser parser, final DateType name) {
		this.dateMatchPattern = dateMatchPattern;
		this.parser = parser;
		this.name = name;
	}

	public List<DatedPhrase> findPhrases(final String phrase) {
		final List<DatedPhrase> result = new ArrayList<>();
		final Matcher matcher = dateMatchPattern.matcher(phrase);
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
		return name.name() + " \""+ dateMatchPattern.pattern()+"\"";
	}

}
