package time.analyser.filter;

import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;

/**
 * Filtre pour phrase.
 */
public class PhraseFilter {

	private int maxLength;
	private int minLength;
	private String[] phraseMustNotStartWith;

	@Inject
	public PhraseFilter(@Named("conf") Conf conf) {
		this.minLength = conf.getMinLength();
		this.maxLength = conf.getMaxLength();
		this.phraseMustNotStartWith = conf.getPhraseMustNotStartWith();
	}

	public boolean keep(final String phrase) {
		boolean startWithCategories = phraseMustNotStartWith != null && Arrays.stream(phraseMustNotStartWith).anyMatch(phrase::startsWith);
		boolean tooLong = phrase.length() > maxLength;
		boolean tooShort = phrase.length() < minLength;

		return !startWithCategories && !tooLong && !tooShort;
	}
}
