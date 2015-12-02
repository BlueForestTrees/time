package time.transformer.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.repo.bean.FullPhrase;
import time.repo.bean.Page;
import time.transformer.component.filter.PageFilter;
import time.transformer.component.filter.PhraseFilter;
import time.transformer.component.reader.FinDuScanException;
import time.transformer.tool.IStorage;
import time.transformer.tool.phrasefinder.DateFinder;

@Service
public class FindPhrasesModule implements IModule {

    private static final Logger LOG = LogManager.getLogger(FindPhrasesModule.class);

    @Autowired
    IStorage storage;

    @Autowired
    PageReaderService pageReader;

    @Autowired
    PageFilter pageFilter;

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
    private DateFinder annee2DotFinder;

    private static final String[] excludeAfter = new String[] { "Notes et références[", "Bibliographie[", "Liens externes[", "Bibliographie[", "Annexes[" };

    private static final Pattern splitPhrasePattern = Pattern.compile("(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +");

    private final Pattern splitParagraphPattern = Pattern.compile("[\r\n\t]+");

    @Override
    public void onStart() throws IOException {
        LOG.info("onStart()");
        storage.start();
    }

    @Override
    public long run(long pageCount) throws IOException, FinDuScanException {
        long phraseCount = 0;
        for (long i = 0; i < pageCount; i++) {
            Page page = pageReader.getNextPage();
            if (pageFilter.isValidPage(page)) {
                if (pageFilter.isNewPage(page)) {
                    phraseCount += handle(page);
                }
                pageFilter.rememberThisPage(page);
            }
        }
        return phraseCount;
    }

    protected long handle(Page page) throws IOException {
        long count = 0;
        final String text = getCleanText(page.getPageContent());

        final String[] paragraphs = getParagraphs(text);

        for (String paragraph : paragraphs) {

            final String[] phrases = getPhrases(paragraph);

            count += handlePage(page, phrases, milliardFinder);
            count += handlePage(page, phrases, millionFinder);
            count += handlePage(page, phrases, annee2DotFinder);
            count += handlePage(page, phrases, jcFinder);
            count += handlePage(page, phrases, romanFinder);
        }
        return count;
    }

    protected long handlePage(Page page, String[] phrasesArray, DateFinder finder) throws IOException {
        long count = 0;
        List<FullPhrase> phrases = finder.findPhrasesWithDates(phrasesArray);
        for (FullPhrase phrase : phrases) {
            if (phraseFilter.keepThisPhrase(phrase)) {
                phrase.setPageUrl(page.getUrl());
                storage.store(phrase);
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

    @Override
    public void onEnd() {
        LOG.info("onEnd()");
        try {
            storage.end();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

}
