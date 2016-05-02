package time.transformer.find;

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
import time.transformer.filter.PhraseFilter;

public class TextAnalyser {

	private static final Logger LOG = LogManager.getLogger(TextAnalyser.class);
	private Pattern splitParagraphPattern;
	private Pattern splitPhrasePattern;
	private PhraseFilter phraseFilter;
	private DatedPhrasesFinders finders;

	@Inject
	public TextAnalyser(@Named("conf") final Conf conf, final PhraseFilter phraseFilter, final DatedPhrasesFinders finders) {
		this.phraseFilter = phraseFilter;
		this.splitParagraphPattern = Pattern.compile(conf.getSplitParagraphPattern());
		this.splitPhrasePattern = Pattern.compile(conf.getSplitPhrasePattern());
		this.finders = finders;
	}

	public void analyse(final Text text) throws IOException {
		final String[] paragraphs = splitParagraphPattern.split(text.getTextString());
		for (String paragraph : paragraphs) {
			text.openParagraph();
			final String[] phrases = splitPhrasePattern.split(paragraph);
			for (String phrase : phrases) {
				if (phraseFilter.keep(phrase)) {
					final List<DatedPhrase> datedPhrases = finders.detect(phrase);
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
