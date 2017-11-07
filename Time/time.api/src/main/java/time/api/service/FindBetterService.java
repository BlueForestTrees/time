package time.api.service;

import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindBetterService implements Formatter{

    private static final Logger LOG = LogManager.getLogger(FindBetterService.class);

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private QueryService queryHelper;

    @Autowired
    private Analyzer analyzer;

    public String[] findBetterTerm(final String term) throws IOException {
        final Query query = queryHelper.getFuzzyTermQuery(term);
        if (query == null) {
            return new String[0];
        }

        final Highlighter highlighter = new Highlighter(this, new QueryScorer(query, "text"));
        highlighter.setTextFragmenter(new SimpleFragmenter(0));
        final TopDocs searchResult = indexSearcher.searchAfter(null, query, 10);

        return Arrays.stream(searchResult.scoreDocs).map(res -> fragment(res, highlighter)).distinct().filter(f->f!=null).toArray(String[]::new);
    }

    protected String fragment(final ScoreDoc scoreDoc, final Highlighter highlighter) {
        try {
            return highlighter.getBestFragment(analyzer, "text", indexSearcher.doc(scoreDoc.doc).get("text")).replaceAll("[^A-Za-z]+", "").toLowerCase();
        } catch (Exception e) {
            LOG.error("fragment", e);
        }
        return null;
    }

    @Override
    public String highlightTerm(String originalText, TokenGroup tokenGroup) {
        return originalText;
    }

}
