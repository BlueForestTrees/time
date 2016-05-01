package time.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.DatedPhrase;
import time.repo.bean.Text;
import time.tika.ToPage;
import time.transformer.phrase.filter.PhraseFilter;
import time.transformer.phrase.finder.DatedPhraseDetector;

public class TextParser {

	private static final Logger LOG = LogManager.getLogger(TextParser.class);
	private Pattern splitParagraphPattern;
	private Pattern splitPhrasePattern;
	private PhraseFilter phraseFilter;
	private DatedPhraseDetector datedPhrasesDetector;

	@Inject
	public TextParser(@Named("conf") final Conf conf, final PhraseFilter phraseFilter, final DatedPhraseDetector datedPhrasesDetector) {
		this.phraseFilter = phraseFilter;
		this.splitParagraphPattern = Pattern.compile(conf.getSplitParagraphPattern());
		this.splitPhrasePattern = Pattern.compile(conf.getSplitPhrasePattern());
		this.datedPhrasesDetector = datedPhrasesDetector;
	}

    //parseText(ToPage.from(input));

	protected void parseText(final Text text) throws IOException {
		final String[] paragraphs = splitParagraphPattern.split(text.getTextString());
		for (String paragraph : paragraphs) {
			text.openParagraph();
			final String[] phrases = splitPhrasePattern.split(paragraph);
			for (String phrase : phrases) {
				if (phraseFilter.keepThisPhrase(phrase)) {
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
			}
			text.closeParagraph();
		}
		LOG.info(paragraphs.length + " paragraphes, " + text.nbDatedPhrasesCount() + " phrases");
	}

}
