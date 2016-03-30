package time.transformer.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import time.transformer.reader.SmartScanner;

@Configuration
@ComponentScan(basePackages={ "time.transformer"})
@Import({ DateFinderConfig.class })
public class TransformerConfig {
    

    @Bean
    public String indexPath() {
         return "/home/slimane/time/Histoire/data/indexes/wiki";
        //return "/Time/data/lucene/sapiens";
    }

    
    @Bean
    public String datasourcePath() {
        return "/home/slimane/time/Histoire/data/sources/wiki";
        //return "C:/Time/data/downloader/sapiens";
    }

    @Bean
    public String baseUrl() {
        // return "http://fr.wikipedia.org/wiki/";
        return "";
    }
    
    @Bean
    public Long pageSize() {
        return 100L;
    }

    @Bean
    public Long maxPhrasesToFetch() {
        return -1L;
    }
    
    @Autowired
    private String datasourcePath;
    
    @Bean
    public int urlMaxLength() {
        return 255;
    }

    @Bean
    public Long pageTotal() {
        return 3700000L;
    }

    @Bean
    String sep() {
        return "\\|¨";
    }

    @Bean
    public SmartScanner scanner() throws IOException {
        SmartScanner scanner = new SmartScanner(datasourcePath);
        scanner.setDelimiter(sep());
        return scanner;
    }
}
