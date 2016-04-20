package time.transformer.phrase.filter;

import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;

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

	public boolean keepThisPhrase(final String phrase) {
		boolean startWithCatogories = phraseMustNotStartWith != null && Arrays.stream(phraseMustNotStartWith).anyMatch(phrase::startsWith);
		boolean tooLong = phrase.length() > maxLength;
		boolean tooShort = phrase.length() < minLength;

		return !startWithCatogories && !tooLong && !tooShort;
	}
}
