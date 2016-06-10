package time.web.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.common.base.Optional;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.domain.DatedPhrase;
import time.domain.Metadata;
import time.tool.reference.Fields;
import time.web.bean.Last;
import time.web.bean.LucenePhrase;
import time.web.bean.Phrases;
import time.web.exception.LuceneRuntimeException;

@Service
public class PhraseService {

    private int pageSize = 20;

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private QueryService queryHelper;

    @Autowired
    private Analyzer analyzer;
    
    @Autowired
    private FindBetterService tryWithService;

    private Map<String, Object> cache = new ConcurrentHashMap<>();

    private Sort sortDateAsc = new Sort(new SortField("date", SortField.Type.LONG));

    public Phrases find(final String request, final String field, final Long from,  final Long to, String lastKey) throws IOException {    	
        final Last last = (Last) cache.remove(lastKey);
        final Query query = queryHelper.getQuery(request, field, from, to);
        final Highlighter highlighter = new Highlighter(new QueryScorer(query, "text"));
        highlighter.setTextFragmenter(new NullFragmenter());
        final TopFieldDocs searchResult = indexSearcher.searchAfter(last == null ? null : last.getDoc(), query, pageSize, sortDateAsc, true, true);

        final Phrases phrases = new Phrases();
        if (searchResult.totalHits > 0) {
            // LES PHRASES
            final Integer lastIndex = last == null ? null : last.getLastIndex();
            phrases.setTotal(searchResult.totalHits);
            final List<DatedPhrase> phraseList = Arrays.stream(searchResult.scoreDocs).map(scoreDoc -> buildPhrase(scoreDoc, highlighter)).collect(Collectors.toList());
            phrases.setPhraseList(phraseList);
            // LE LAST
            final int nbPhrasesFound = searchResult.scoreDocs.length;
            final int newLastIndex = lastIndex == null ? nbPhrasesFound - 1 : lastIndex + nbPhrasesFound;
            if (newLastIndex < searchResult.totalHits - 1) {
                final FieldDoc lastScoreDoc = (FieldDoc) searchResult.scoreDocs[nbPhrasesFound - 1];
                final String newLastKey = UUID.randomUUID().toString();
                cache.put(newLastKey, new Last(lastScoreDoc, newLastIndex));
                phrases.setLastKey(newLastKey);
            }
        } else {
            phrases.setTotal(0);
            final String[] alternatives = tryWithService.findBetterTerm(request);
            phrases.setAlternatives(alternatives);
        }
        return phrases;
    }

    private DatedPhrase buildPhrase(final ScoreDoc scoreDoc, final Highlighter highlighter) {
        try {
            final DatedPhrase phrase = new DatedPhrase();
            final LucenePhrase lucenePhrase = LucenePhrase.with(indexSearcher.doc(scoreDoc.doc));

            phrase.setText(Optional.fromNullable(highlighter.getBestFragment(analyzer, Fields.TEXT, lucenePhrase.text())).or(lucenePhrase.text()));
            phrase.setDate(lucenePhrase.date());
            phrase.setUrl(lucenePhrase.url());
            phrase.setType(Optional.fromNullable(lucenePhrase.type()).or(Metadata.Type.WIKI));
            phrase.setTitle(lucenePhrase.title());
            phrase.setAuthor(lucenePhrase.author());

            return phrase;


        } catch (IOException | InvalidTokenOffsetsException e) {
            throw new LuceneRuntimeException(e);
        }
    }



    public String findFirstSlack(final String term){
        return htmlToSlackFormat(findFirst(term));
    }

	private String findFirst(final String term) {
		String result = "";
		final Query query = queryHelper.getQueryForFirstPhrase(term);
		final Sort sort = new Sort(new SortField(Fields.DATE, SortField.Type.LONG));
		
		try {
			final TopFieldDocs searchResult = indexSearcher.search(query, 1, sort);
			if (searchResult.totalHits > 0) {
				final Highlighter highlighter = new Highlighter(new QueryScorer(query, "text"));
		        highlighter.setTextFragmenter(new NullFragmenter());
				result = buildPhrase(searchResult.scoreDocs[0], highlighter).getText();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private String htmlToSlackFormat(final String textWithHtml) {
		final String slackFormattedMessage = textWithHtml.replace("<strong> ", " <strong>").replace(" </strong>", "</strong> ").replace("<b> ", " <b>").replace(" </b>", "</b> ").replace("<B> ", " <B>").replace(" </B>", "</B> ").replace("<B>", "*").replace("</B>", "*").replace("<b>", "*").replace("</b>", "*").replace("<strong>", "*").replace("</strong>", "*");
	
		return "{\"response_type\": \"in_channel\",\"text\":\""+slackFormattedMessage+"\"}";
	}

}
