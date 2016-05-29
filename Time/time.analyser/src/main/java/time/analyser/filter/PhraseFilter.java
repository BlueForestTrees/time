package time.analyser.filter;

import java.util.Arrays;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;

/**
 * Filtre pour phrase.
 */
public class PhraseFilter {

	private static final Logger LOGGER = LogManager.getLogger(PhraseFilter.class);

	private int phraseMaxLength;
	private int phraseMinLength;
	private String[] phraseMustNotStartWith;

	@Inject
	public PhraseFilter(final Conf conf) {
		this.phraseMinLength = conf.getPhraseMinLength();
		this.phraseMaxLength = conf.getPhraseMaxLength();
		this.phraseMustNotStartWith = conf.getPhraseMustNotStartWith();
		LOGGER.info(this);
	}

	public boolean keep(final String phrase) {
		boolean startWithCategories = phraseMustNotStartWith != null && Arrays.stream(phraseMustNotStartWith).anyMatch(phrase::startsWith);
		boolean tooLong = phrase.length() > phraseMaxLength;
		boolean tooShort = phrase.length() < phraseMinLength;

		return !startWithCategories && !tooLong && !tooShort;
	}

	@Override
	public String toString() {
		return "PhraseFilter{" +
				"phraseMaxLength=" + phraseMaxLength +
				", phraseMinLength=" + phraseMinLength +
				", phraseMustNotStartWith=" + Arrays.toString(phraseMustNotStartWith) +
				'}';
	}
}
