package time.analyser.filter;

import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.TextAnalyser;
import time.conf.Conf;

/**
 * Filtre pour phrase.
 */
public class PhraseFilter {

	private static final Logger LOGGER = LogManager.getLogger(PhraseFilter.class);

	private int maxLength;
	private int minLength;
	private String[] phraseMustNotStartWith;

	@Inject
	public PhraseFilter(final Conf conf) {
		this.minLength = conf.getMinLength();
		this.maxLength = conf.getMaxLength();
		this.phraseMustNotStartWith = conf.getPhraseMustNotStartWith();
		LOGGER.info(this);
	}

	public boolean keep(final String phrase) {
		boolean startWithCategories = phraseMustNotStartWith != null && Arrays.stream(phraseMustNotStartWith).anyMatch(phrase::startsWith);
		boolean tooLong = phrase.length() > maxLength;
		boolean tooShort = phrase.length() < minLength;

		return !startWithCategories && !tooLong && !tooShort;
	}

	@Override
	public String toString() {
		return "PhraseFilter{" +
				"maxLength=" + maxLength +
				", minLength=" + minLength +
				", phraseMustNotStartWith=" + Arrays.toString(phraseMustNotStartWith) +
				'}';
	}
}
