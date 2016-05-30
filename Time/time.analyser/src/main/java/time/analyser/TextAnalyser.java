package time.analyser;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import time.analyser.filter.PhraseFilter;
import time.analyser.finder.DatedPhrasesFinders;
import time.domain.Analyser;
import time.domain.DatedPhrase;
import time.domain.Text;
import time.tool.string.Strings;

public class TextAnalyser {

	private static final Logger LOGGER = LogManager.getLogger(TextAnalyser.class);
	private Pattern splitParagraphPattern;
	private Pattern splitPhrasePattern;
	private PhraseFilter phraseFilter;
	private DatedPhrasesFinders finders;

	@Inject
	public TextAnalyser(final Analyser analyser, final PhraseFilter phraseFilter, final DatedPhrasesFinders finders) {
		this.phraseFilter = phraseFilter;
		this.splitParagraphPattern = Pattern.compile(Optional.ofNullable(analyser.getSplitParagraphPattern()).orElse("[\r\n\t]+"));
		this.splitPhrasePattern = Pattern.compile(Optional.ofNullable(analyser.getSplitPhrasePattern()).orElse("(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))(\\.|\\?|!)) +"));
		this.finders = finders;
        LOGGER.info(this);
	}

	public Text analyse(final Text text) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("analyse " + text.getUrl());
        }
		final String[] paragraphs = splitParagraphPattern.split(text.getTextString());
		text.setParagraphs(paragraphs);
		for (String paragraph : paragraphs) {
			text.openParagraph();
			final String[] phrases = splitPhrasePattern.split(paragraph);
			for (String phrase : phrases) {
				if (phraseFilter.keep(phrase)) {
					final List<DatedPhrase> datedPhrases = finders.detect(phrase);
					if (!datedPhrases.isEmpty()) {
						text.startPhrase();
						datedPhrases.forEach(dp -> text.appendHightlightContent(dp.getText()));
						text.endPhrase();
					}else{
						text.appendHightlightContent(phrase);
						text.appendHightlightContent(" ");
					}
					text.addPhrases(datedPhrases);
				}
			}
			text.closeParagraph();
		}
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(text.getParagraphs().length + " paragraphes, " + text.getPhrases().size() + " phrases");
        }
		return text;
	}

    @Override
    public String toString() {
        return Strings.noReturns("TextAnalyser{" +
                "splitParagraphPattern=" + splitParagraphPattern +
                ", splitPhrasePattern=" + splitPhrasePattern +
                ", phraseFilter=" + phraseFilter +
                ", finders=" + finders +
                '}');
    }
}
