package time.transformer.tool.handler;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.repo.bean.Page;
import time.repo.bean.Phrase;
import time.transformer.component.filter.PhraseFilter;
import time.transformer.repo.PhraseRepository;
import time.transformer.tool.phrasefinder.DateFinder;

@Component
public class PhraseHandler {

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    PhraseFilter phraseFilter;

    @Autowired
    private DateFinder milliardFinder;

    @Autowired
    private DateFinder millionFinder;

    @Autowired
    private DateFinder jcFinder;

    @Autowired
    private DateFinder romanFinder;

    @Autowired
    private DateFinder enanneFinder;

    @Autowired
    private DateFinder annee2DotFinder;

    private final String[] excludeAfter = new String[] { "Notes et références[", "Bibliographie[", "Liens externes[", "Bibliographie[", "Annexes[" };

    private final String splitPhraseRegex = "(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +";
    private final Pattern splitPhrasePattern = Pattern.compile(splitPhraseRegex);

    private final String splitParagraphRegex = "[\r\n\t]+";
    private final Pattern splitParagraphPattern = Pattern.compile(splitParagraphRegex);

    public long handle(Page page) {
        long count = 0;
        final String text = getCleanText(page.getPageContent());

        final String[] paragraphs = getParagraphs(text);

        for (String paragraph : paragraphs) {

            final String[] phrases = getPhrases(paragraph);

            count += handlePage(page, phrases, milliardFinder);
            count += handlePage(page, phrases, millionFinder);
            count += handlePage(page, phrases, annee2DotFinder);
            count += handlePage(page, phrases, enanneFinder);
            count += handlePage(page, phrases, jcFinder);
            count += handlePage(page, phrases, romanFinder);
        }
        return count;
    }

    protected long handlePage(Page page, String[] phrasesArray, DateFinder finder) {
        long count = 0;
        List<Phrase> phrases = finder.findPhrasesWithDates(phrasesArray);
        for (Phrase phrase : phrases) {
            if (phraseFilter.keepThisPhrase(phrase)) {
                phrase.setPageId(page.getId());

                System.out.println("\"" + phrase.getText() + "\",");

                phraseRepository.save(phrase);
                count++;
            }
        }
        return count;
    }

    public String[] getPhrases(final String text) {
        return splitPhrasePattern.split(text);
    }

    public String[] getParagraphs(final String text) {
        return splitParagraphPattern.split(text);
    }

    /**
     * Renvoie le texte de 0 à la première position trouvée parmi
     * {@link #excludeAfter}
     * 
     * @param text
     * @return
     */
    public String getCleanText(final String text) {
        final OptionalInt whereToCut = Arrays.stream(excludeAfter).mapToInt(term -> text.indexOf(term)).filter(v -> v > 0).min();
        if (whereToCut.isPresent()) {
            return text.substring(0, whereToCut.getAsInt());
        } else {
            return text;
        }
    }

}
