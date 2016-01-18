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

import time.repo.bean.Phrase;
import time.repo.bean.Page;
import time.transformer.component.filter.PageFilter;
import time.transformer.component.filter.PhraseFilter;
import time.transformer.component.reader.FinDuScanException;
import time.transformer.tool.LuceneStorage;
import time.transformer.tool.phrasefinder.DateFinder;

@Service
public class FindPhrasesModule implements IModule {

    private static final Logger LOG = LogManager.getLogger(FindPhrasesModule.class);

    @Autowired
    LuceneStorage storage;

    @Autowired
    PageReaderService pageReader;

    @Autowired
    PageFilter pageFilter;

    @Autowired
    PhraseFilter phraseFilter;

    @Autowired
    DateFinder[] finders;

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
            final Page page = pageReader.getNextPage();
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
        transformPage(page);
        final String text = page.getPageContent();

        final String[] paragraphs = getParagraphs(text);

        for (String paragraph : paragraphs) {

            final String[] phrases = getPhrases(paragraph);

            for(DateFinder finder : finders){
                count += handlePage(page, phrases, finder);
            }
        }
        return count;
    }

    protected long handlePage(Page page, String[] phrasesArray, DateFinder finder) throws IOException {
        long count = 0;
        List<Phrase> phrases = finder.findPhrasesWithDates(phrasesArray);
        for (Phrase phrase : phrases) {
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
     * @param page
     * @return
     */
    private void transformPage(final Page page)
    {
        page.setPageContent(getCleanText(page.getPageContent()));
        //TODO si https://fr.wikipedia.org/wiki/141_av._J.-C. => années négatives (_av._J.-C. négative)
        //TODO si https://fr.wikipedia.org/wiki/Ann%C3%A9es_100 => décennies (_av._J.-C. négative)
        //TODO si https://fr.wikipedia.org/wiki/IIe_si%C3%A8cle => siècle (_av._J.-C. négative)
        //TODO si https://fr.wikipedia.org/wiki/Ier_mill%C3%A9naire => millenaire (_av._J.-C. négative)

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
