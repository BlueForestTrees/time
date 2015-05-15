package wiki.tool.handler;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wiki.component.util.PageMemRepo;
import wiki.entity.Page;
import wiki.entity.Phrase;
import wiki.repo.PhraseRepository;
import wiki.tool.phrasefinder.Datation;

@Component
public class PhraseHandler {

	@Autowired
	PhraseRepository phraseRepository;

	@Autowired
	PageMemRepo pageMemRepo;

	private final String splitPhraseRegex = "(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +";
	private final Pattern splitPhrasePattern = Pattern.compile(splitPhraseRegex);

	private final String splitParagraphRegex = "[\r\n\t]+";
	private final Pattern splitParagraphPattern = Pattern.compile(splitParagraphRegex);

	public String[] getPhrases(String text) {
		return splitPhrasePattern.split(text);
	}

	public String[] getParagraphs(String text) {
		return splitParagraphPattern.split(text);
	}

	public void handle(Page page) {
		final String text = page.getText();
		final String[] paragraphs = getParagraphs(text);

		for (String paragraph : paragraphs) {

			final String[] phrases = getPhrases(paragraph);

			handlePage(page, phrases, Datation.MILLIARD);
			handlePage(page, phrases, Datation.MILLION);
//			handlePageBy(page, phrases, Datation.ANNEE2DOT);
//			handlePageBy(page, phrases, Datation.ENANNEE);
//			handlePageBy(page, phrases, Datation.JC);
//			handlePageBy(page, phrases, Datation.ROMAN);
		}
	}

	public void handlePage(Page page, String[] phrasesArray, Datation finder) {
		List<Phrase> phrases = finder.findPhrase(phrasesArray);
		for (Phrase phrase : phrases) {
			if (pageMemRepo.keepThisPhrase(phrase)) {
				System.out.println(phrase.getType() + "___" + pageMemRepo.normalizedUrl(page));
				System.out.println(phrase.getText());
			}
			// phrase.setPageId(page.getId());
		}
		// phraseRepository.save(phrases);
	}

}
