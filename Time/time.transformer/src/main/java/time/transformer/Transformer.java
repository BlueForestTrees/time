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
	public Transformer(@Named("conf") Conf conf, LuceneStorage storage, PhraseFilter phraseFilter) {
		this.storage = storage;
		this.phraseFilter = phraseFilter;
		String pattern = conf.getSplitParagraphPattern();
		this.splitParagraphPattern = Pattern.compile(pattern);
		this.datedPhrasesDetector = new DatedPhraseDetector();
	}

	public void onStart() throws IOException {
		LOG.info("onStart()");
		storage.start();
	}

	protected long handlePage(final Page page) throws IOException {
		long phrasesCount = 0;
		final String[] paragraphs = splitParagraphPattern.split(page.getTextString());
		for (String paragraph : paragraphs) {
			phrasesCount += handleParagraph(page, paragraph);
		}
		LOG.info(paragraphs.length + " paragraphes, " + phrasesCount + " phrases");
		
		return phrasesCount;
	}

	private long handleParagraph(final Page page, final String paragraph) throws IOException {
		long phrasesCount = 0;
		page.openParagraph();
		final String[] phrases = splitPhrasePattern.split(paragraph);
		for (String phrase : phrases) {
			phrasesCount += handlePhrase(page, phrase);
		}
		page.closeParagraph();
		return phrasesCount;
	}

	private long handlePhrase(final Page page, final String phrase) {
		long phrasesCount = 0;
		if (phraseFilter.keepThisPhrase(phrase)) {
			final List<DatedPhrase> datedPhrases = datedPhrasesDetector.detect(phrase);
			if (!datedPhrases.isEmpty()) {
				page.startPhrase();
			}
			for (DatedPhrase datedPhrase : datedPhrases) {
				datedPhrase.setPageUrl(page.getUrl());
				storage.store(datedPhrase);
				phrasesCount++;
			}
			if (!datedPhrases.isEmpty()) {
				page.endPhrase();
			}else{
				page.appendHightlightContent(phrase);
			}
		}
		return phrasesCount;
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
