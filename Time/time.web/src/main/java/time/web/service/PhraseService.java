package time.web.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.repo.bean.Phrase;
import time.web.bean.Last;
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
    private QueryService queryHelper;

    @Autowired
    private Map<String, Object> cache;

    @Autowired
    private Sort sortDateAsc;

    public Phrases find(final Scale scale, final Long bucketValue, final String term, String lastKey) throws IOException {
        final String bucketName = scale.getField();
        final Last last = (Last) cache.remove(lastKey);
        final Query query = queryHelper.getQuery(term, bucketName, bucketValue, null);
        final TopFieldDocs search = indexSearcher.searchAfter(last == null ? null : last.getDoc(), query, pageSize, sortDateAsc, true, true);

        return toPhrases(search, last == null ? null : last.getLastIndex());
    }

    protected Phrases toPhrases(final TopDocs searchResult, final Integer lastIndex) {
        // LES PHRASES
        final Phrases phrases = new Phrases();
        final List<Phrase> phraseList = Arrays.stream(searchResult.scoreDocs).map(scoreDoc -> getPhrase(scoreDoc)).collect(Collectors.toList());
        phrases.setPhraseList(phraseList);
        // LE LAST INDEX
        final int nbPhrasesFound = searchResult.scoreDocs.length;
        final int newLastIndex = lastIndex == null ? nbPhrasesFound - 1 : lastIndex + nbPhrasesFound;
        if (newLastIndex < searchResult.totalHits - 1) {
            // LE LAST SCORE
            final FieldDoc lastScoreDoc = (FieldDoc) searchResult.scoreDocs[nbPhrasesFound - 1];
            final String lastKey = UUID.randomUUID().toString();
            cache.put(lastKey, new Last(lastScoreDoc, newLastIndex));
            phrases.setLastKey(lastKey);
        }
        return phrases;
    }

    protected Phrase getPhrase(ScoreDoc scoreDoc) {
        try {
            final Document doc = indexSearcher.doc(scoreDoc.doc);
            final Phrase phrase = new Phrase();
            phrase.setText(doc.get("text"));
            phrase.setPageUrl(doc.get("pageUrl"));
            phrase.setDate((long) doc.getField("date").numericValue());
            return phrase;
        } catch (IOException e) {
            throw new LuceneRuntimeException(e);
        }
    }
}
