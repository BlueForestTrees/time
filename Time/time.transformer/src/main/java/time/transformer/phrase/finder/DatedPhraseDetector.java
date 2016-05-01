package time.transformer.phrase.finder;

import time.repo.bean.DatedPhrase;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import time.repo.bean.Text;

public class DatedPhraseDetector {
	
	private PhraseFinder[] finders;

	@Inject
	public DatedPhraseDetector(@Named("finders") final PhraseFinder[] finders) {
		this.finders = finders;
	}

	public List<DatedPhrase> detect(final Text text, final String rawPhrase) {
		final List<DatedPhrase> result = new ArrayList<>();
		for (PhraseFinder finder : finders) {
			result.addAll(finder.findPhrases(text, rawPhrase));
		}
		return result;
	}
}
