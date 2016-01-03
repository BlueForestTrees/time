package time.web.config;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LuceneConfig {

    @Bean
    public String indexPath() {
        return "/Time/data/lucene/phrases";
        //return "/Time/data/lucene/sapiens";
    }

    @Bean
    public FSDirectory indexDir() throws IOException {
        return FSDirectory.open(FileSystems.getDefault().getPath(indexPath()));
    }

    @Bean
    public DirectoryReader directoryReader() throws IOException {
        return DirectoryReader.open(indexDir());
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
    public Map<String, Object> cache() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Sort sortDateAsc() {
        return new Sort(new SortField("date", Type.LONG));
    }
    
    @Bean
    public Analyzer analyzer(){
        return new StandardAnalyzer();
    }

}
