package time.transformer.phrase.finder;

import time.repo.bean.DatedPhrase;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import time.repo.bean.Page;

public class DatedPhraseDetector {
	
	private PhraseFinder[] finders;

	@Inject
	public DatedPhraseDetector(@Named("finders") final PhraseFinder[] finders) {
		this.finders = finders;
	}

	public List<DatedPhrase> detect(final Page page, final String rawPhrase) {
		final List<DatedPhrase> result = new ArrayList<>();
		for (PhraseFinder finder : finders) {
			result.addAll(finder.findPhrases(page, rawPhrase));
		}
		return result;
	}
}
