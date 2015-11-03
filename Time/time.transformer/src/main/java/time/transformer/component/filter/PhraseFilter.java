package time.transformer.component.filter;

import org.springframework.stereotype.Component;

import time.repo.bean.Phrase;

@Component
public class PhraseFilter {

    public boolean keepThisPhrase(Phrase phrase) {
        boolean startWithCatogories = phrase.getText().startsWith("Catégories :");
        boolean tooLong = phrase.getText().length() > 1000;
        return !startWithCatogories && !tooLong;
    }
}
