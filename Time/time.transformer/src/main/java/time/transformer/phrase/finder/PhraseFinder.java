package time.transformer.phrase.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import time.repo.bean.DatedPhrase;
import time.repo.bean.Page;
import time.transformer.phrase.finder.parser.IParser;

public class PhraseFinder {

	private final Pattern pattern;
	private final IParser parser;
	private final String name;

	public PhraseFinder(Pattern pattern, IParser parser, String name) {
		this.pattern = pattern;
		this.parser = parser;
		this.name = name;
	}

	public List<DatedPhrase> findPhrases(final Page page, final String phrase) {
		final List<DatedPhrase> result = new ArrayList<>();
		final Matcher matcher = pattern.matcher(phrase);
		while (matcher.find()) {
			final String dateExtract = matcher.group();
			final Long date = parser.from(matcher);
			if (date != null) {
				final DatedPhrase datedPhrase = new DatedPhrase();
				datedPhrase.setText(preparePhrase(phrase, dateExtract));
				datedPhrase.setDate(date);
				datedPhrase.setPageUrl(page.getUrl());
				result.add(datedPhrase);
			}
		}
		return result;
	}

	protected String preparePhrase(final String phrase, final String dateExtract) {
		return phrase.replace(dateExtract, "<b>" + dateExtract + "</b>").replaceAll("\\[.*?\\]", "");
	}

	@Override
	public String toString() {
		return name;
	}

}
