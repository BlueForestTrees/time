package time.transformer.phrase.filter;

import org.springframework.stereotype.Component;

import time.repo.bean.Phrase;

@Component
public class PhraseFilter {
	
	final static int MAX_LENGTH = 250;
	final static int MIN_LENGTH = 50;

    public boolean keepThisPhrase(Phrase phrase) {
        boolean startWithCatogories = phrase.getText().startsWith("Catégories :");
		boolean tooLong = phrase.getText().length() > MAX_LENGTH;
        boolean tooShort = phrase.getText().length() < MIN_LENGTH;

        return !startWithCatogories && !tooLong && !tooShort;
    }
}
