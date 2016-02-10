package time.transformer;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.repo.bean.Page;
import time.repo.bean.Phrase;
import time.transformer.page.IPageTransformer;
import time.transformer.page.filter.PageFilter;
import time.transformer.phrase.filter.PhraseFilter;
import time.transformer.phrase.finder.PhraseFinder;
import time.transformer.reader.FinDuScanException;
import time.transformer.reader.PageReader;
import time.transformer.storage.LuceneStorage;

@Component
public class TransformerService {

    private static final Logger LOG = LogManager.getLogger(TransformerService.class);

    @Autowired
    LuceneStorage storage;

    @Autowired
    PageReader pageReader;

    @Autowired
    PageFilter pageFilter;

    @Autowired
    PhraseFilter phraseFilter;

    @Autowired
    PhraseFinder[] finders;
    
    @Autowired
    IPageTransformer pageTransformer;

    private static final Pattern splitPhrasePattern = Pattern.compile("(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +");

    private final Pattern splitParagraphPattern = Pattern.compile("[\r\n\t]+");

    public void onStart() throws IOException {
        LOG.info("onStart()");
        storage.start();
    }

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

    protected long handle(final Page page) throws IOException {
        long count = 0;
        prepare(page);
        final String text = page.getTextString();
        final String[] paragraphs = getParagraphs(text);
        for (String paragraph : paragraphs) {
            final String[] phrases = getPhrases(paragraph);
            for(PhraseFinder finder : finders){
                count += findAndStorePhrases(page, phrases, finder);
            }
        }
        return count;
    }

    protected long findAndStorePhrases(Page page, String[] phrasesArray, PhraseFinder finder) throws IOException {
        long count = 0;
        final List<Phrase> phrases = finder.findPhrases(phrasesArray);
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
    private void prepare(final Page page)
    {
        pageTransformer.transform(page);
        //TODO si https://fr.wikipedia.org/wiki/141_av._J.-C. => années négatives (_av._J.-C. négative)
        //TODO si https://fr.wikipedia.org/wiki/Ann%C3%A9es_100 => décennies (_av._J.-C. négative)
        //TODO si https://fr.wikipedia.org/wiki/IIe_si%C3%A8cle => siècle (_av._J.-C. négative)
        //TODO si https://fr.wikipedia.org/wiki/Ier_mill%C3%A9naire => millenaire (_av._J.-C. négative)
    }

    public void onEnd() {
        LOG.info("onEnd()");
        try {
            storage.end();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

}
