package time.web.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.repo.bean.Phrase;
import time.web.bean.Last;
import time.web.bean.Phrases;
import time.web.exception.LuceneRuntimeException;

@Service
public class PhraseService {

    @Autowired
    private int pageSize;

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private QueryService queryHelper;

    @Autowired
    private Map<String, Object> cache;

    @Autowired
    private Sort sortDateAsc;

    @Autowired
    private Analyzer analyzer;
    
    @Autowired
    private FindBetterService tryWithService;

    //TODO remplacer scale+bucket par date.
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
            final List<Phrase> phraseList = Arrays.stream(searchResult.scoreDocs).map(scoreDoc -> getPhrase(scoreDoc, highlighter)).collect(Collectors.toList());
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
    
	public String findFirstSlack(final String term){
		return htmlToSlackFormat(findFirst(term));
	}

    protected Phrase getPhrase(final ScoreDoc scoreDoc, final Highlighter highlighter) {
        try {
            final Document doc = indexSearcher.doc(scoreDoc.doc);
            final Phrase phrase = new Phrase();
            final String text = highlighter.getBestFragment(analyzer, "text", doc.get("text"));

            phrase.setText(text != null ? text : doc.get("text"));
            phrase.setPageUrl(doc.get("pageUrl"));
            phrase.setDate((long) doc.getField("date").numericValue());
            return phrase;
        } catch (IOException | InvalidTokenOffsetsException e) {
            throw new LuceneRuntimeException(e);
        }
    }

	protected String findFirst(final String term) {
		String result = "";
		final Query query = queryHelper.getQueryForFirstPhrase(term);
		final Sort sort = new Sort(new SortField("date", Type.LONG));
		
		try {
			final TopFieldDocs searchResult = indexSearcher.search(query, 1, sort);
			if (searchResult.totalHits > 0) {
				final Highlighter highlighter = new Highlighter(new QueryScorer(query, "text"));
		        highlighter.setTextFragmenter(new NullFragmenter());
				result = getPhrase(searchResult.scoreDocs[0], highlighter).getText();
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
