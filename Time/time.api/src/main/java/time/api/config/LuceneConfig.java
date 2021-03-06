package time.api.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import time.api.lucene.RandomComparator;
import time.tool.reference.Fields;

import java.io.IOException;
import java.nio.file.FileSystems;

@Configuration
public class LuceneConfig {

    private static final Logger LOGGER = LogManager.getLogger(LuceneConfig.class);

    @Value("${indexDir:./time.data/indexes/mergeables/wiki}")
    private String indexDir;

    @Value("${searchPhrasePageSize:20}")
    private Integer theSearchPhrasePageSize;

    @Bean
    protected Integer searchPhrasePageSize(){
        return theSearchPhrasePageSize;
    }

    @Bean
    public Analyzer analyzer() {
        return new FrenchAnalyzer();
    }

    @Bean
    public QueryParser textQueryParser(){
        return new QueryParser("text",analyzer());
    }

    @Bean
    public IndexSearcher indexSearcher() throws IOException {
        final IndexSearcher indexSearcher = new IndexSearcher(directoryReader());
        LOGGER.info("{} docs in index", indexSearcher.count(new MatchAllDocsQuery()));
        return indexSearcher;
    }

    @Bean
    protected DirectoryReader directoryReader() throws IOException {
        LOGGER.info("serving index {}", indexDir);
        final FSDirectory fsDirectory = FSDirectory.open(FileSystems.getDefault().getPath(indexDir));
        return DirectoryReader.open(fsDirectory);
    }

    @Bean
    protected Sort dateAscSort() {
        return new Sort(new SortField(Fields.DATE, SortField.Type.LONG));
    }

    @Bean
    protected Sort dateDescSort() {
        return new Sort(new SortField(Fields.DATE, SortField.Type.LONG, true));
    }

    @Bean
    protected Sort randomSort() {
        return new Sort(
                new SortField(
                        "",
                        new FieldComparatorSource() {

                            @Override
                            public FieldComparator<Integer> newComparator(String fieldname, int numHits, int sortPos, boolean reversed) {
                                return new RandomComparator();
                            }

                        }
                )
        );
    }

}
