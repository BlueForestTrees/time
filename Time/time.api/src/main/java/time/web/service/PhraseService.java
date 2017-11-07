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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;
import static time.web.bean.TermPeriodFilter.fromString;

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
        final TermPeriodFilter termPeriodFilter = fromString(request);
        // && lastKey == null car on fait une recherche en linkMode que pour le premier appel, les suivants sont une requête normale.
        if (termPeriodFilter.isLinkMode() && lastKey == null) {
            return linkModeSearch(termPeriodFilter);
        } else {
            return normalSearch(request, lastKey, termPeriodFilter);
        }
    }

    private Phrases linkModeSearch(final TermPeriodFilter termPeriodFilter) throws IOException{
        LOGGER.info("linkModeSearch {}", termPeriodFilter);
        // requete firstDate sur chacun des deux mots 'left' et 'right'
        final String leftWord = termPeriodFilter.getWords().split(" ")[0];
        final String rightWord = termPeriodFilter.getWords().split(" ")[1];
        final Long leftDate = findFirstDate(leftWord);
        final Long rightDate = findFirstDate(rightWord);
        LOGGER.info("leftWord/date {}/{} rightWord/date {}/{}", leftWord, leftDate, rightWord, rightDate);
        if (leftDate == null || rightDate == null) {
            return null;
        }
        // prendre le max des deux (ex. max is left)
        boolean maxIsLeft = leftDate > rightDate;
        final TermPeriodFilter copy = termPeriodFilter.copy();
        // requete 5 phrases right < dateMaxLeft
        if (maxIsLeft) {
            LOGGER.info("max is Left");
            copy.setTo(leftDate);
            copy.setWords(rightWord);
        } else {
            LOGGER.info("max is Right");
            copy.setTo(rightDate);
            copy.setWords(leftWord);
        }
        final Query introQuery = queryHelper.getQuery(copy);
        final TopFieldDocs introDocs = doSearch(introQuery, linkModePageSize);
        final List<DatedPhrase> introDatedPhrases = getDatedPhrases(introQuery, introDocs);

        LOGGER.info("nb phrase d'intro: {}", introDatedPhrases.size());

        //requete normale >= dateMaxLeft
        //TODO devrait être inclusif
        termPeriodFilter.setFrom(leftDate);
        final Query leftRightMixQuery = queryHelper.getQuery(copy);
        final TopFieldDocs leftRightMixDocs = doSearch(leftRightMixQuery, null);
        final List<DatedPhrase> leftRightMixDatedPhrases = getDatedPhrases(leftRightMixQuery, leftRightMixDocs);


        final List<DatedPhrase> datedPhrases = new ArrayList<>(leftRightMixDatedPhrases);
        datedPhrases.addAll(introDatedPhrases);
        // LES PHRASES
        final Phrases phrases = new Phrases();
        phrases.setTotal(introDocs.totalHits + leftRightMixDocs.totalHits);
        phrases.setPhraseList(datedPhrases);
        // LE LAST
        final String lastKey = buildLast(null, leftRightMixDocs);
        phrases.setLastKey(lastKey);

        return phrases;
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
        final Phrases phrases = new Phrases();
        final List<DatedPhrase> phraseList = getDatedPhrases(query, searchResult);

        phrases.setTotal(searchResult.totalHits);
        phrases.setPhraseList(phraseList);
        phrases.setLastKey(buildLast(last, searchResult));

        return phrases;
    }

    private String buildLast(final Last last, final TopFieldDocs searchResult){
        final int nbPhrasesFound = searchResult.scoreDocs.length;
        final Integer lastIndex = last == null ? null : last.getLastIndex();
        final int newLastIndex = lastIndex == null ? nbPhrasesFound - 1 : lastIndex + nbPhrasesFound;
        if (newLastIndex < searchResult.totalHits - 1) {
            final FieldDoc lastScoreDoc = (FieldDoc) searchResult.scoreDocs[nbPhrasesFound - 1];
            final String newLastKey = randomUUID().toString();
            cache.save(newLastKey, lastScoreDoc, newLastIndex);
            return newLastKey;
        }else{
            return null;
        }
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
        final Query query = queryHelper.getQuery(fromString(term));
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
        final Query query = queryHelper.getQuery(fromString(term));
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
