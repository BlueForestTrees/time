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
import time.repo.bean.Page;
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

	protected void handlePage(final Page page) throws IOException {
		final String[] paragraphs = splitParagraphPattern.split(page.getTextString());
		for (String paragraph : paragraphs) {
			handleParagraph(page, paragraph);
		}
		LOG.info(paragraphs.length + " paragraphes, " + page.nbDatedPhrasesCount() + " phrases");
	}

	private void handleParagraph(final Page page, final String paragraph) throws IOException {
		page.openParagraph();
		final String[] phrases = splitPhrasePattern.split(paragraph);
		for (String phrase : phrases) {
			if (phraseFilter.keepThisPhrase(phrase)) {
				handlePhrase(page, phrase);
			}
		}
		page.closeParagraph();
	}

	private void handlePhrase(final Page page, final String phrase) {
		final List<DatedPhrase> datedPhrases = datedPhrasesDetector.detect(page, phrase);
		if (!datedPhrases.isEmpty()) {
			page.startPhrase();
		}
		page.appendHightlightContent(phrase);
		if (!datedPhrases.isEmpty()) {
			page.endPhrase();
		}

		page.addPhrases(datedPhrases);
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
