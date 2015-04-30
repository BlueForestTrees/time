package wiki.tool.handler;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wiki.entity.Page;
import wiki.entity.Phrase;
import wiki.repo.PhraseRepository;
import wiki.tool.phrasefinder.PhraseFinder;


@Component
public class PhraseHandler {
	
	@Autowired
	PhraseRepository phraseRepository;
	
	private final String notEndWithThis = "av|mr|dr|jc|JC|J\\.-C";
	private final String sep = "[ \r\n]+";
	private final String splitRegex = "(?<=(?<!( ("+notEndWithThis+")))\\.)"+sep;
	private final Pattern pattern = Pattern.compile(splitRegex);
	
	
	public String[] getPhrases(String text){
		return pattern.split(text);
	}
	
	
	public void handlePhrase(Page page){
		final String text = page.getText();
		final String[] phrases = getPhrases(text);
		
		handlePageBy(page, phrases, PhraseFinder.MILLIARD);
		handlePageBy(page, phrases, PhraseFinder.MILLION);
		handlePageBy(page, phrases, PhraseFinder.ANNEE2DOT);
		handlePageBy(page, phrases, PhraseFinder.ENANNEE);
		handlePageBy(page, phrases, PhraseFinder.JC);
		handlePageBy(page, phrases, PhraseFinder.ROMAN);
	}
	
	public void handlePageBy(Page page, String[] phrasesArray, PhraseFinder finder){
		List<Phrase> phrases = finder.findPhrase(phrasesArray, page);
		phraseRepository.save(phrases);	
	}
	
}
