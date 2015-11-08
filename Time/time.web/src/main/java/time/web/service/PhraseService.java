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
import time.repo.bean.ScoreDocDTO;
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

    public Phrases find(final Scale scale, final Long bucketValue, final String term, final ScoreDocDTO after) throws IOException {
        final String bucketName = scale.getField();

        Query query = getQuery(term, bucketName, bucketValue);
        TopDocs search = indexSearcher.searchAfter(toScoreDoc(after), query, pageSize);

        return toPhrases(search);
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

    protected Phrases toPhrases(final TopDocs searchResult) {
        final Phrases phrases = new Phrases();

        final List<Phrase> phraseList = Arrays.stream(searchResult.scoreDocs).map(scoreDoc -> getPhrase(scoreDoc)).collect(Collectors.toList());

        phrases.setPhraseList(phraseList);

        ScoreDoc scoreDoc = searchResult.scoreDocs[searchResult.scoreDocs.length - 1];

        phrases.setLastScoreDoc(toScoreDocDTO(scoreDoc));

        return phrases;
    }

    protected ScoreDoc toScoreDoc(ScoreDocDTO scoreDTO) {
        if (scoreDTO == null)
            return null;
        return new ScoreDoc(scoreDTO.getDoc(), scoreDTO.getScore(), scoreDTO.getShardIndex());
    }

    protected ScoreDocDTO toScoreDocDTO(ScoreDoc scoreDoc) {
        ScoreDocDTO dto = new ScoreDocDTO();

        dto.setDoc(scoreDoc.doc);
        dto.setScore(scoreDoc.score);
        dto.setShardIndex(scoreDoc.shardIndex);

        return dto;
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
