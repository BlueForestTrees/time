package time.transformer.tool.phrasefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import time.repo.bean.FullPhrase;
import time.transformer.tool.parser.IParser;

public class DateFinder {

    private final Pattern pattern;
    private final IParser parser;

    public DateFinder(Pattern pattern, IParser parser) {
        this.pattern = pattern;
        this.parser = parser;
    }

    /**
     * Renvoie une {@link List<FullPhrase>} de Phrase detectées.
     * 
     * @param phrases
     * @return
     */
    public List<FullPhrase> findPhrasesWithDates(String[] phrases) {
        final List<FullPhrase> result = new ArrayList<>();
        for (String phraseString : phrases) {
            final Matcher matcher = pattern.matcher(phraseString);
            while (matcher.find()) {
                final String dateExtract = matcher.group();
                final Long date = parser.from(matcher);
                final FullPhrase phrase = new FullPhrase();
                phrase.setText(phraseString.replace(dateExtract, "<strong>" + dateExtract + "</strong>"));
                phrase.setDate(date);
                result.add(phrase);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "DateFinder<"+parser.getClass().getSimpleName()+">";
    }

}
