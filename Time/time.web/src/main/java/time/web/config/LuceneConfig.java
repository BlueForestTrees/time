package time.web.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import time.conf.Conf;
import time.conf.Resolver;

import java.io.IOException;
import java.nio.file.FileSystems;

@Configuration
public class LuceneConfig {

    @Autowired
    private Conf conf;

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
        final String indexDir = Resolver.get(conf.getIndexDir());
        final FSDirectory fsDirectory = FSDirectory.open(FileSystems.getDefault().getPath(indexDir));
        return DirectoryReader.open(fsDirectory);
    }

}
