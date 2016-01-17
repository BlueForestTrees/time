package time.web.service;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindBetterService implements Formatter{

    private static final Logger LOG = LoggerFactory.getLogger(FindBetterService.class);

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private QueryService queryHelper;

    @Autowired
    private Analyzer analyzer;

    public String findBetterTerm(final String term) throws IOException {
        final Query query = queryHelper.getFuzzyTermQuery(term);
        if (query == null) {
            return null;
        }

        final Highlighter highlighter = new Highlighter(this, new QueryScorer(query, "text"));
        highlighter.setTextFragmenter(new SimpleFragmenter(0));
        final TopDocs searchResult = indexSearcher.searchAfter(null, query, 1);

        if (searchResult.totalHits > 0) {
            try {
                return highlighter.getBestFragment(analyzer, "text", firstIn(searchResult).get("text"));
            } catch (InvalidTokenOffsetsException e) {
                LOG.error("findBetterTerm, hightlighter.bestFragments", e);
            }
        }
        return null;
    }

    protected Document firstIn(final TopDocs searchResult) throws IOException {
        return indexSearcher.doc(searchResult.scoreDocs[0].doc);
    }

    @Override
    public String highlightTerm(String originalText, TokenGroup tokenGroup) {
        return originalText;
    }

}
