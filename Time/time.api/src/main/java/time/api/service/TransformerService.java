package time.api.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import time.domain.DatedPhrase;
import time.domain.Metadata;
import time.tool.reference.Fields;
import time.api.bean.LucenePhrase;

import java.io.IOException;

import static java.util.Optional.ofNullable;

@Service
public class TransformerService {

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private Analyzer analyzer;

    public DatedPhrase buildPhrase(final ScoreDoc scoreDoc, final Highlighter highlighter) {
        try {
            final DatedPhrase phrase = new DatedPhrase();
            final Document doc = indexSearcher.doc(scoreDoc.doc);
            final LucenePhrase lucenePhrase = LucenePhrase.with(doc);

            phrase.setText(ofNullable(highlighter.getBestFragment(analyzer, Fields.TEXT, lucenePhrase.text())).orElse(lucenePhrase.text()));
            phrase.setDate(lucenePhrase.date());
            phrase.setUrl(lucenePhrase.url());
            phrase.setType(ofNullable(lucenePhrase.type()).orElse(Metadata.Type.WIKI));
            phrase.setTitle(lucenePhrase.title());
            phrase.setAuthor(lucenePhrase.author());

            return phrase;

        } catch (IOException | InvalidTokenOffsetsException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getDate(final ScoreDoc scoreDoc) {
        try {
            final Document doc = indexSearcher.doc(scoreDoc.doc);
            final LucenePhrase lucenePhrase = LucenePhrase.with(doc);
            return lucenePhrase.date();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
