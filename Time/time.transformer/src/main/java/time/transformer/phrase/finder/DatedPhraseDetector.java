package time.transformer.phrase.finder;

import java.util.ArrayList;
import java.util.List;

import time.repo.bean.DatedPhrase;
import time.transformer.factory.DateFindersFactory;

public class DatedPhraseDetector {
	
	private PhraseFinder[] finders;

	@Inject
	public DatedPhraseDetector(final PhraseFinder[] finders) {
		this.finders = finders;
	}

	/**
	 * Renvoie une {@link List<Phrase>} de Phrase detectées.
	 * 
	 * @param rawPhrases
	 * @return
	 */
	public List<DatedPhrase> detect(final String rawPhrase) {
		final List<DatedPhrase> result = new ArrayList<>();
		for (PhraseFinder finder : finders) {
			result.addAll(finder.findPhrases(rawPhrase));
		}
		return result;
	}
	
	protected String preparePhrase(final String phrase, final String dateExtract) {
		return phrase.replace(dateExtract, "<b>" + dateExtract + "</b>").replaceAll("\\[.*?\\]", "");
	}
}
