package time.web.config;

import com.google.common.base.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import time.conf.Resolver;
import time.domain.TimeWebConf;
import time.tool.reference.Fields;
import time.web.lucene.RandomComparator;

import java.io.IOException;
import java.nio.file.FileSystems;

@Configuration
public class LuceneConfig {

    private static final Logger LOGGER = LogManager.getLogger(LuceneConfig.class);

    @Autowired
    private TimeWebConf conf;

    @Bean
    public Analyzer analyzer() {
        return new StandardAnalyzer();
    }

    @Bean
    public IndexSearcher indexSearcher() throws IOException {
        final IndexSearcher indexSearcher = new IndexSearcher(directoryReader());
        LOGGER.info("{} docs in index", indexSearcher.count(new MatchAllDocsQuery()));
        return indexSearcher;
    }

    @Bean
    public SortedSetDocValuesReaderState readerState() throws IOException {
        return new DefaultSortedSetDocValuesReaderState(directoryReader());
    }

    @Bean
    protected DirectoryReader directoryReader() throws IOException {
        final String indexDir = Resolver.get(conf.getIndexDir());
        LOGGER.info("serving index {}", indexDir);
        final FSDirectory fsDirectory = FSDirectory.open(FileSystems.getDefault().getPath(indexDir));
        return DirectoryReader.open(fsDirectory);
    }

    @Bean
    public Integer searchPhrasePageSize() throws IOException {
        return Optional.fromNullable(conf.getSearchPhrasePageSize()).or(20);
    }

    @Bean
    protected Sort dateSort() {
        return new Sort(new SortField(Fields.DATE, SortField.Type.LONG));
    }

    @Bean
    protected Sort randomSort() {
        return new Sort(
                new SortField(
                        "",
                        new FieldComparatorSource() {

                            @Override
                            public FieldComparator<Integer> newComparator(String fieldname, int numHits, int sortPos, boolean reversed) throws IOException {
                                return new RandomComparator();
                            }

                        }
                )
        );
    }

}
