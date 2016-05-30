package time.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Analyser {
	private String[] phraseMustNotStartWith;
	private Integer phraseMinLength;
	private Integer phraseMaxLength;
	private String splitParagraphPattern;
	private String splitPhrasePattern;

	public String[] getPhraseMustNotStartWith() {
		return phraseMustNotStartWith;
	}

	public Integer getPhraseMinLength() {
		return phraseMinLength;
	}

	public Integer getPhraseMaxLength() {
		return phraseMaxLength;
	}

	public String getSplitParagraphPattern() {
		return splitParagraphPattern;
	}

	public String getSplitPhrasePattern() {
		return splitPhrasePattern;
	}
}
