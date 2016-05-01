package time.transformer;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.DatedPhrase;
import time.repo.bean.Text;
import time.transformer.phrase.filter.PhraseFilter;
import time.transformer.phrase.finder.DatedPhraseDetector;
import time.transformer.storage.LuceneStorage;

public class Transformer {

	private static final Logger LOG = LogManager.getLogger(Transformer.class);
	private static final Pattern splitPhrasePattern = Pattern.compile("(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +");

	private Pattern splitParagraphPattern;
	private LuceneStorage storage;
	private PhraseFilter phraseFilter;
	private DatedPhraseDetector datedPhrasesDetector;

	@Inject
	public Transformer(@Named("conf") final Conf conf, final LuceneStorage storage, final PhraseFilter phraseFilter, final DatedPhraseDetector datedPhrasesDetector) {
		this.storage = storage;
		this.phraseFilter = phraseFilter;
		this.splitParagraphPattern = Pattern.compile(conf.getSplitParagraphPattern());
		this.datedPhrasesDetector = datedPhrasesDetector;
	}

	public void onStart() throws IOException {
		LOG.info("onStart()");
		storage.start();
	}

	protected void handlePage(final Text text) throws IOException {
		final String[] paragraphs = splitParagraphPattern.split(text.getTextString());
		for (String paragraph : paragraphs) {
			handleParagraph(text, paragraph);
		}
		LOG.info(paragraphs.length + " paragraphes, " + text.nbDatedPhrasesCount() + " phrases");
	}

	private void handleParagraph(final Text text, final String paragraph) throws IOException {
		text.openParagraph();
		final String[] phrases = splitPhrasePattern.split(paragraph);
		for (String phrase : phrases) {
			if (phraseFilter.keepThisPhrase(phrase)) {
				handlePhrase(text, phrase);
			}
		}
		text.closeParagraph();
	}

	private void handlePhrase(final Text text, final String phrase) {
		final List<DatedPhrase> datedPhrases = datedPhrasesDetector.detect(text, phrase);
		if (!datedPhrases.isEmpty()) {
			text.startPhrase();
		}
		text.appendHightlightContent(phrase);
		if (!datedPhrases.isEmpty()) {
			text.endPhrase();
		}

		text.addPhrases(datedPhrases);
	}

	public void onEnd() {
		LOG.info("onEnd()");
		try {
			storage.end();
		} catch (IOException e) {
			LOG.error(e);
		}
	}

}
