package wiki.tool.handler;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wiki.component.util.PageMemRepo;
import wiki.entity.Page;
import wiki.entity.Phrase;
import wiki.repo.PhraseRepository;
import wiki.tool.phrasefinder.DateFinder;

@Component
public class PhraseHandler {

	@Autowired
	PhraseRepository phraseRepository;

	@Autowired
	PageMemRepo pageMemRepo;

	@Autowired
	private DateFinder milliardFinder;
	
	@Autowired
	private DateFinder millionFinder;
	
	@Autowired
	private DateFinder jcFinder;
	
	@Autowired
	private DateFinder romanFinder;
	
	@Autowired
	private DateFinder enanneFinder;
	
	@Autowired
	private DateFinder annee2DotFinder;

	private final String splitByNoteRegex = "Notes et références";
	private final Pattern splitByNote = Pattern.compile(splitByNoteRegex);

	private final String splitPhraseRegex = "(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +";
	private final Pattern splitPhrasePattern = Pattern.compile(splitPhraseRegex);

	private final String splitParagraphRegex = "[\r\n\t]+";
	private final Pattern splitParagraphPattern = Pattern.compile(splitParagraphRegex);


	public String[] getPhrases(final String text) {
		return splitPhrasePattern.split(text);
	}

	public String[] getParagraphs(final String text) {
		return splitParagraphPattern.split(text);
	}

	private String[] getByNote(final String text) {
		final String[] byNotes =  splitByNote.split(text);
		if(byNotes.length > 1){
			return Arrays.copyOfRange(byNotes, 0, byNotes.length -1);
		}else{
			return byNotes;
		}
		
	}

	public long handle(Page page) {
		long count = 0;
		final String text = page.getText();
		final String[] byNotes = getByNote(text);

		for (String byNote : byNotes) {

			final String[] paragraphs = getParagraphs(byNote);

			for (String paragraph : paragraphs) {

				final String[] phrases = getPhrases(paragraph);

				count+=handlePage(page, phrases, milliardFinder);
				count+=handlePage(page, phrases, millionFinder);
				// handlePageBy(page, phrases, Datation.ANNEE2DOT);
				// handlePageBy(page, phrases, Datation.ENANNEE);
				count+=handlePage(page, phrases, jcFinder);
				//count+=handlePage(page, phrases, Datation.ROMAN);
			}
		}
		return count;
	}

	public long handlePage(Page page, String[] phrasesArray, DateFinder finder) {
		long count = 0;
		List<Phrase> phrases = finder.findPhrasesWithDates(phrasesArray);
		for (Phrase phrase : phrases) {
			if (pageMemRepo.keepThisPhrase(phrase)) {
				phrase.setPageId(page.getId());
				phraseRepository.save(phrase);
				//System.out.println(phrase.getType() + "___" + pageMemRepo.normalizedUrl(page));
				//System.out.println(phrase.getText());
				count++;
			}
		}
		return count;
	}

}
