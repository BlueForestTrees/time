package time.transformer.tool.phrasefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import time.repo.bean.Phrase;
import time.transformer.tool.parser.IParser;

public class DateFinder {

    private final Pattern pattern;
    private final IParser parser;

    public DateFinder(Pattern pattern, IParser parser) {
        this.pattern = pattern;
        this.parser = parser;
    }

    /**
     * Renvoie une {@link List<Phrase>} de Phrase detectées.
     * 
     * @param phrases
     * @return
     */
    public List<Phrase> findPhrasesWithDates(String[] phrases) {
        final List<Phrase> result = new ArrayList<>();
        for (String text : phrases) {
            final Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                final String dateExtract = matcher.group();
                final Long date = parser.from(matcher);
                final Phrase phrase = new Phrase();
                phrase.setText(preparePhrase(text, dateExtract));
                phrase.setDate(date);
                result.add(phrase);
            }
        }
        return result;
    }

    protected String preparePhrase(final String phrase, final String dateExtract) {
        return phrase.replace(dateExtract, "<strong>" + dateExtract + "</strong>").replaceAll("\\[.*?\\]", "");
    }

    @Override
    public String toString() {
        return "DateFinder<"+parser.getClass().getSimpleName()+">";
    }

}
