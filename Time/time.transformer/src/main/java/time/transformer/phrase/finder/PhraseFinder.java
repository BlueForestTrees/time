package time.transformer.phrase.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import time.repo.bean.Phrase;
import time.transformer.phrase.finder.parser.IParser;

public class PhraseFinder {

    private final Pattern pattern;
    private final IParser parser;
    private final String name;

    public PhraseFinder(Pattern pattern, IParser parser, String name) {
        this.pattern = pattern;
        this.parser = parser;
        this.name = name;
    }

    /**
     * Renvoie une {@link List<Phrase>} de Phrase detectées.
     * 
     * @param rawPhrases
     * @return
     */
    public List<Phrase> findPhrases(String[] rawPhrases) {
        final List<Phrase> result = new ArrayList<>();
        for (String text : rawPhrases) {
            final Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                final String dateExtract = matcher.group();
                final Long date = parser.from(matcher);
                if(date != null){
	                final Phrase phrase = new Phrase();
	                phrase.setText(preparePhrase(text, dateExtract));
	                phrase.setDate(date);
	                result.add(phrase);
                }
            }
        }
        return result;
    }

    protected String preparePhrase(final String phrase, final String dateExtract) {
        return phrase.replace(dateExtract, "<b>" + dateExtract + "</b>").replaceAll("\\[.*?\\]", "");
    }

    @Override
    public String toString() {
        return name;
    }

}
