package time.transformer.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.transformer.storage.LuceneStorage;

@Configuration
public class LuceneStorageConfig {

    @Bean
    public String indexPath(){
        return "C:/Time/data/testphrases";
    }
    
    @Bean
    public LuceneStorage storage(){
        return new LuceneStorage();
    }
    
}