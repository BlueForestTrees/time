package time.analyser.filter;

import java.util.Arrays;
import java.util.Optional;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Analyser;
import time.domain.Conf;

/**
 * Filtre pour phrase.
 */
public class PhraseFilter {

	private static final Logger LOGGER = LogManager.getLogger(PhraseFilter.class);

	private int phraseMaxLength;
	private int phraseMinLength;
	private String[] phraseMustNotStartWith;

	@Inject
	public PhraseFilter(final Analyser analyser) {
		this.phraseMinLength = analyser.getPhraseMinLength();
		this.phraseMaxLength = analyser.getPhraseMaxLength();
		this.phraseMustNotStartWith = analyser.getPhraseMustNotStartWith();
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
