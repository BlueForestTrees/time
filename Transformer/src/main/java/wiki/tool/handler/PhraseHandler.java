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
	
	private final String splitRegex = "((?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +)|([\r\n\t]+)";
	private final Pattern pattern = Pattern.compile(splitRegex);
	
	
	public String[] getPhrases(String text){
		return pattern.split(text);
	}
	
	
	public void handlePhrase(Page page){
		final String text = page.getText();
		final String[] phrases = getPhrases(text);
		
		handlePageBy(page, phrases, Datation.MILLIARD);
		handlePageBy(page, phrases, Datation.MILLION);
		//handlePageBy(page, phrases, Datation.ANNEE2DOT);
		//handlePageBy(page, phrases, Datation.ENANNEE);
		handlePageBy(page, phrases, Datation.JC);
		handlePageBy(page, phrases, Datation.ROMAN);
	}
	
	public void handlePageBy(Page page, String[] phrasesArray, Datation finder){
		List<Phrase> phrases = finder.findPhrase(phrasesArray);
		for(Phrase phrase : phrases){
			if(pageMemRepo.keepThisPhrase(phrase)){
				System.out.println("__"+phrase.getType()+"___"+page.getUrl()+"_______");
				System.out.println(phrase.getText());
				System.out.println("_________________________________________________");
			}
			//phrase.setPageId(page.getId());
		}
		//phraseRepository.save(phrases);	
	}
	
}
