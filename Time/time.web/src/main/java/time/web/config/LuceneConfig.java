package time.web.config;

import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LuceneConfig {
    
    @Bean
    public String indexPath(){
        return "/Time/data/allphrases";
    }
    
    @Bean
    public FSDirectory indexDir() throws IOException{
        return FSDirectory.open(FileSystems.getDefault().getPath(indexPath()));
    }
    
    @Bean
    public DirectoryReader directoryReader() throws IOException{
        return DirectoryReader.open(indexDir());
    }
    
    @Bean
    public IndexSearcher indexSearcher() throws IOException{
        return new IndexSearcher(directoryReader());
    }
    
    @Bean
    public SortedSetDocValuesReaderState readerState() throws IOException{
        return new DefaultSortedSetDocValuesReaderState(directoryReader());
    }
    
    @Bean
    public FacetsCollector facetsCollector(){
        return new FacetsCollector();
    }
}
