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
import time.transformer.phrase.filter.PhraseFilter;
import time.transformer.phrase.finder.PhraseFinder;
import time.transformer.storage.LuceneStorage;

public class TransformerService {

    private static final Logger LOG = LogManager.getLogger(TransformerService.class);
    private static final Pattern splitPhrasePattern = Pattern.compile("(?<=(?<!( (av|mr|dr|jc|JC|J\\.-C)))\\.) +");

    private Pattern splitParagraphPattern;
    private LuceneStorage storage;
    private PhraseFinder[] finders;
    private PhraseFilter phraseFilter;
    
    @Inject
    public TransformerService(@Named("conf") Conf conf, LuceneStorage storage, PhraseFilter phraseFilter){
    	this.storage = storage;
    	this.phraseFilter = phraseFilter;
    	String pattern = conf.getSplitParagraphPattern();
    	this.splitParagraphPattern = Pattern.compile(pattern);
    	finders = new DateFindersFactory().finders();
    }

    public void onStart() throws IOException {
        LOG.info("onStart()");
        storage.start();
    }

    protected long handle(final Page page) throws IOException {
        long phrasesCount = 0;
        final String[] paragraphs = splitParagraphPattern.split(page.getTextString());
		for (String paragraph : paragraphs) {
			page.appendHightlightContent("<div class=\"paragraph\">");
            final String[] phrases = splitPhrasePattern.split(paragraph);
            for(PhraseFinder finder : finders){
                phrasesCount += findAndStorePhrases(page, paragraph, phrases, finder);
            }
            page.appendHightlightContent("</div>");
        }
		LOG.info(paragraphs.length + " paragraphes, " + phrasesCount + " phrases dans ");
        return phrasesCount;
    }

    protected long findAndStorePhrases(final Page page, final String paragraph, final String[] phrasesArray, final PhraseFinder finder) throws IOException {
        long count = 0;
        //TODO appendHightlight phrases
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

    public void onEnd() {
        LOG.info("onEnd()");
        try {
            storage.end();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

}
