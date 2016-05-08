package time.analyser.analyse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.analyser.filter.PhraseFilter;
import time.analyser.find.DatedPhrasesFinders;
import time.repo.bean.DatedPhrase;
import time.repo.bean.Text;

public class TextAnalyser {

	private static final Logger LOG = LogManager.getLogger(TextAnalyser.class);
	private Pattern splitParagraphPattern;
	private Pattern splitPhrasePattern;
	private PhraseFilter phraseFilter;
	private DatedPhrasesFinders finders;

	@Inject
	public TextAnalyser(final PhraseFilter phraseFilter,@Named("finders") final DatedPhrasesFinders finders) {
		this.phraseFilter = phraseFilter;
		this.splitParagraphPattern = Pattern.compile("[\r\n\t]+");
		this.splitPhrasePattern = Pattern.compile("(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))(\\.|\\?|!)) +");
		this.finders = finders;
	}

	public Text analyse(final Text text) throws IOException {
		final String[] paragraphs = splitParagraphPattern.split(text.getTextString());
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
					}
					datedPhrases.stream().forEach(p -> p.setPageUrl(text.getUrl()));
					text.addPhrases(datedPhrases);
				}
			}
			text.closeParagraph();
		}
		LOG.info(paragraphs.length + " paragraphes, " + text.nbDatedPhrasesCount() + " phrases");
		return text;
	}

}
