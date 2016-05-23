package time.web.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import time.conf.Args;

import java.io.IOException;
import java.nio.file.FileSystems;

@Configuration
public class LuceneConfig {

    @Bean
    public Analyzer analyzer(){
        return new StandardAnalyzer();
    }

    @Bean
    public IndexSearcher indexSearcher() throws IOException {
        return new IndexSearcher(directoryReader());
    }

    @Bean
    public SortedSetDocValuesReaderState readerState() throws IOException {
        return new DefaultSortedSetDocValuesReaderState(directoryReader());
    }

    @Bean
    protected DirectoryReader directoryReader() throws IOException {
        final String indexPath = new Args().getEnvSubstitutor().replace("${TIME_HOME}/indexes/prod/");
        final FSDirectory indexDir = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
        return DirectoryReader.open(indexDir);
    }

}
