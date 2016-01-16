package time.transformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ TransformerConfig.class })
public class FindPhrasesConfig {

    @Bean
    public String indexPath() {
        // return "/Time/data/lucene/phrases";
        return "/Time/data/lucene/sapiens";
    }

    
    @Bean
    public String datasourcePath() {
        // return "C:/Time/data/downloader/pages/all";
        return "C:/Time/data/downloader/sapiens";
    }

    @Bean
    public String baseUrl() {
        // return "http://fr.wikipedia.org/wiki/";
        return "";
    }
    
    @Bean
    public Long pageSize() {
        return 1000L;
    }

    @Bean
    public Long maxPhrasesToFetch() {
        return -1L;
    }

}
