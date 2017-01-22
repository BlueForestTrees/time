package time.web.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import time.domain.DatedPhrase;
import time.web.bean.Last;
import time.web.bean.Phrases;
import time.web.bean.TermPeriodFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Service
public class PhraseService {

    private static final Logger LOGGER = LogManager.getLogger(PhraseService.class);

    @Autowired
    private Sort dateAscSort;

    @Autowired
    private Sort dateDescSort;

    @Autowired
    private Sort randomSort;

    @Autowired
    private Integer searchPhrasePageSize;

    private int linkModePageSize = 5;

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private QueryService queryHelper;

    @Autowired
    private FindBetterService tryWithService;

    @Autowired
    private TransformerService transformerService;

    @Autowired
    private CacheService cache;

    public Phrases find(final String request, String lastKey) throws IOException {
        final TermPeriodFilter termPeriodFilter = TermPeriodFilter.fromString(request);
        if (termPeriodFilter.isLinkMode()) {
            return linkModeSearch(termPeriodFilter);
        } else {
            return normalSearch(request, lastKey, termPeriodFilter);
        }
    }

    private Phrases linkModeSearch(final TermPeriodFilter termPeriodFilter) throws IOException{
        // requete firstDate sur chacun des deux mots 'left' et 'right'
        final String leftWord = termPeriodFilter.getWords().split(" ")[0];
        final String rightWord = termPeriodFilter.getWords().split(" ")[1];
        final Long leftDate = findFirstDate(leftWord);
        final Long rightDate = findFirstDate(rightWord);
        if (leftDate == null || rightDate == null) {
            return null;
        }
        // prendre le max des deux (ex. max is left)
        boolean maxIsLeft = leftDate > rightDate;
        final TermPeriodFilter copy = termPeriodFilter.copy();
        // requete 5 phrases right < dateMaxLeft
        if (maxIsLeft) {
            copy.setTo(leftDate);
            copy.setWords(rightWord);
        } else {
            copy.setTo(rightDate);
            copy.setWords(leftWord);
        }
        final Query introQuery = queryHelper.getQuery(copy);
        final TopFieldDocs introDocs = doSearch(introQuery, linkModePageSize);
        final List<DatedPhrase> introDatedPhrases = getDatedPhrases(introQuery, introDocs);

        //requete normale >= dateMaxLeft
        //TODO devrait être inclusif
        termPeriodFilter.setFrom(leftDate);
        final Query leftRightMixQuery = queryHelper.getQuery(copy);
        //TODO gérer le last, si on a un last il ne faut pas faire de linkMode...
        final TopFieldDocs leftRightMixDocs = doSearch(leftRightMixQuery, last);
        final List<DatedPhrase> leftRightMixDatedPhrases = getDatedPhrases(leftRightMixQuery, leftRightMixDocs);



        return null;
    }

    private Phrases normalSearch(String request, String lastKey, TermPeriodFilter termPeriodFilter) throws IOException {
        final Query query = queryHelper.getQuery(termPeriodFilter);
        final Last last = cache.pop(lastKey);
        final TopFieldDocs searchResult = doSearch(query, last);

        if (searchResult.totalHits > 0) {
            return phrases(query, last, searchResult);
        } else {
            return emptyPhrases(request);
        }
    }

    private TopFieldDocs doSearch(final Query query, final int pageSize) throws IOException {
        return indexSearcher.searchAfter(null, query, pageSize, dateAscSort, true, true);
    }

    private TopFieldDocs doSearch(final Query query, final Last last) throws IOException {
        return indexSearcher.searchAfter(last == null ? null : last.getDoc(), query, searchPhrasePageSize, dateAscSort, true, true);
    }

    private Phrases emptyPhrases(String request) throws IOException {
        final Phrases phrases = new Phrases();
        phrases.setTotal(0);
        final String[] alternatives = tryWithService.findBetterTerm(request);
        phrases.setAlternatives(alternatives);
        return phrases;
    }

    private Phrases phrases(Query query, Last last, TopFieldDocs searchResult) {
        // LES PHRASES
        final Phrases phrases = new Phrases();
        final List<DatedPhrase> phraseList = getDatedPhrases(query, searchResult);

        phrases.setTotal(searchResult.totalHits);
        phrases.setPhraseList(phraseList);

        // LE LAST
        final int nbPhrasesFound = searchResult.scoreDocs.length;
        final Integer lastIndex = last == null ? null : last.getLastIndex();
        final int newLastIndex = lastIndex == null ? nbPhrasesFound - 1 : lastIndex + nbPhrasesFound;
        if (newLastIndex < searchResult.totalHits - 1) {
            final FieldDoc lastScoreDoc = (FieldDoc) searchResult.scoreDocs[nbPhrasesFound - 1];
            final String newLastKey = randomUUID().toString();
            cache.save(newLastKey, lastScoreDoc, newLastIndex);
            phrases.setLastKey(newLastKey);
        }
        return phrases;
    }

    private List<DatedPhrase> getDatedPhrases(final Query query, final TopFieldDocs searchResult) {
        final Highlighter highlighter = queryHelper.getHighlighter(query);
        return Arrays.stream(searchResult.scoreDocs).map(scoreDoc -> transformerService.buildPhrase(scoreDoc, highlighter)).collect(Collectors.toList());
    }

    public DatedPhrase findFirst(final String term) {
        return findOne(term, dateAscSort);
    }

    public DatedPhrase findLast(final String term) {
        return findOne(term, dateDescSort);
    }

    public DatedPhrase findRandom(final String term) {
        return findOne(term, randomSort);
    }

    private DatedPhrase findOne(final String term, final Sort sort) {
        DatedPhrase result = null;
        final Query query = queryHelper.getQuery(TermPeriodFilter.fromString(term));
        try {
            final TopFieldDocs searchResult = indexSearcher.search(query, 1, sort);
            if (searchResult.totalHits > 0) {
                final ScoreDoc scoreDoc = searchResult.scoreDocs[0];
                final Highlighter highlighter = new Highlighter(new QueryScorer(query, "text"));
                highlighter.setTextFragmenter(new NullFragmenter());
                result = transformerService.buildPhrase(scoreDoc, highlighter);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return result;
    }

    private Long findFirstDate(final String term) {
        return findOneDate(term, dateAscSort);
    }

    private Long findOneDate(final String term, final Sort sort) {
        Long date = null;
        final Query query = queryHelper.getQuery(TermPeriodFilter.fromString(term));
        try {
            final TopFieldDocs searchResult = indexSearcher.search(query, 1, sort);
            if (searchResult.totalHits > 0) {
                date = transformerService.getDate(searchResult.scoreDocs[0]);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return date;
    }

}
