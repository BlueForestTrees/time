package time.transformer.phrase.filter;

import org.springframework.stereotype.Component;

import time.repo.bean.Phrase;

@Component
public class PhraseFilter {

    public boolean keepThisPhrase(Phrase phrase) {
        boolean startWithCatogories = phrase.getText().startsWith("Catégories :");
        boolean tooLong = phrase.getText().length() > 1000;
        boolean tooShort = phrase.getText().length() < 100;

        return !startWithCatogories && !tooLong && !tooShort;
    }
}
