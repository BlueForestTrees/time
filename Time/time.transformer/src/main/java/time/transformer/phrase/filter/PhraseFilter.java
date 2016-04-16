package time.transformer.phrase.filter;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.Phrase;

public class PhraseFilter {

	private int maxLength;
	private int minLength;

	@Inject
	public PhraseFilter(@Named("conf") Conf conf) {
		this.minLength = conf.getMinLength();
		this.maxLength = conf.getMaxLength();
	}

	public boolean keepThisPhrase(Phrase phrase) {
		boolean startWithCatogories = phrase.getText().startsWith("Catégories :");
		boolean tooLong = phrase.getText().length() > maxLength;
		boolean tooShort = phrase.getText().length() < minLength;

		return !startWithCatogories && !tooLong && !tooShort;
	}
}
