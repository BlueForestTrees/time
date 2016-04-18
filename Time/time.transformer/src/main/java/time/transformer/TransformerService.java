package time.transformer;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.Page;
import time.repo.bean.Phrase;
import time.transformer.factory.DateFindersFactory;
import time.transformer.page.filter.PageFilter;
import time.transformer.page.transformer.IPageTransformer;
import time.transformer.phrase.filter.PhraseFilter;
import time.transformer.phrase.finder.PhraseFinder;
import time.transformer.reader.FinDuScanException;
import time.transformer.reader.PageReader;
import time.transformer.storage.LuceneStorage;

public class TransformerService {

    private static final Logger LOG = LogManager.getLogger(TransformerService.class);
    private static final Pattern splitPhrasePattern = Pattern.compile("(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +");
    private static final Pattern splitParagraphPattern = Pattern.compile("[\r\n\t]+");

    private LuceneStorage storage;
    private PhraseFinder[] finders;
    private PhraseFilter phraseFilter;
    private PageReader pageReader;
    private PageFilter pageFilter;
    private IPageTransformer pageTransformer;
    
    @Inject
    public TransformerService(@Named("conf") Conf conf, LuceneStorage storage, PhraseFilter phraseFilter, PageReader pageReader, PageFilter pageFilter, IPageTransformer pageTransformer){
    	this.storage = storage;
    	this.phraseFilter = phraseFilter;
    	this.pageReader = pageReader;
    	this.pageFilter = pageFilter;
    	this.pageTransformer = pageTransformer;
    	finders = new DateFindersFactory().finders();
    }

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
    	LOG.info(page.getTextString());
        long phrasesCount = 0;
        pageTransformer.transform(page);

        final String[] paragraphs = getParagraphs(page.getTextString());
        LOG.info(paragraphs.length + " paragraphes");
		for (String paragraph : paragraphs) {
            final String[] phrases = getPhrases(paragraph);
            //LOG.info(phrases.length + " phrases");
            for(PhraseFinder finder : finders){
            	//TODO stocker les paragraphes zippés, avec l'id du paragraphe dans les phrases
                phrasesCount += findAndStorePhrases(page, paragraph, phrases, finder);
            }
        }
        return phrasesCount;
    }

    protected long findAndStorePhrases(final Page page, final String paragraph, final String[] phrasesArray, final PhraseFinder finder) throws IOException {
        long count = 0;
        final List<Phrase> phrases = finder.findPhrases(phrasesArray);
        //LOG.info(phrases.size() + " dated phrases");
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

    public void onEnd() {
        LOG.info("onEnd()");
        try {
            storage.end();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

}
