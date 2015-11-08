package time.web.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import time.repo.bean.Phrase;
import time.web.bean.Phrases;
import time.web.enums.Scale;
import time.web.exception.LuceneRuntimeException;

@Service
public class PhraseService {

    @Autowired
    private int pageSize;

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private SortedSetDocValuesReaderState readerState;

    @Autowired
    private FacetsCollector facetsCollector;
    
    public Phrases find(final Scale scale, final Long bucketValue, final String term, final Integer doc, final Float score, Integer lastIndex) throws IOException {
        final String bucketName = scale.getField();
        final ScoreDoc after = lastIndex == null ? null : new ScoreDoc(doc, score);

        Query query = getQuery(term, bucketName, bucketValue);
        TopDocs search = indexSearcher.searchAfter(after, query, pageSize);
        return toPhrases(search, lastIndex);
    }

    public Query getQuery(String term, String bucketName, Long bucketValue) {
        boolean noBucket = StringUtils.isEmpty(bucketName) || bucketValue == null;
        boolean noTerm = StringUtils.isEmpty(term);

        if (noBucket && noTerm) {
            return new MatchAllDocsQuery();
        }

        TermQuery textQuery = noTerm ? null : new TermQuery(new Term("text", term));
        Query bucketQuery = noBucket ? null : NumericRangeQuery.newLongRange(bucketName, bucketValue, bucketValue, true, true);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if (textQuery != null) {
            builder.add(textQuery, Occur.MUST);
        }
        if (bucketQuery != null) {
            builder.add(bucketQuery, Occur.MUST);
        }
        return builder.build();
    }

    protected Phrases toPhrases(final TopDocs searchResult, Integer lastIndex) {
        final Phrases phrases = new Phrases();
        final int nbPhrasesFound = searchResult.scoreDocs.length;

        final List<Phrase> phraseList = Arrays.stream(searchResult.scoreDocs).map(scoreDoc -> getPhrase(scoreDoc)).collect(Collectors.toList());

        phrases.setPhraseList(phraseList);

        ScoreDoc lastScoreDoc = searchResult.scoreDocs[nbPhrasesFound-1];

        Integer newLastIndex = lastIndex == null ? nbPhrasesFound-1 : lastIndex + nbPhrasesFound;
        if(newLastIndex == searchResult.totalHits-1){
            newLastIndex = null;
        }
        phrases.setDoc(lastScoreDoc.doc);
        phrases.setScore(lastScoreDoc.score);
        phrases.setLastIndex(newLastIndex);

        return phrases;
    }

    protected Phrase getPhrase(ScoreDoc scoreDoc) {
        try {
            final Document doc = indexSearcher.doc(scoreDoc.doc);
            final Phrase phrase = new Phrase();
            phrase.setText(doc.get("text"));
            phrase.setDate((long) doc.getField("date").numericValue());
            return phrase;
        } catch (IOException e) {
            throw new LuceneRuntimeException(e);
        }
    }

}
