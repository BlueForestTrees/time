package time.transformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import time.transformer.service.FindPhrasesModule;
import time.transformer.service.IModule;

@Configuration
@Import({ TransformerConfig.class })
public class FindPhrasesConfig {

    
    public class Source{
        private String indexPath;
        /*
         * //return "/Time/data/lucene/phrases";
        return "/Time/data/lucene/sapiens";
         */
        private String datasourcePath;
        //return "C:/Time/data/downloader/pages/all";
        // return "C:/Time/data/downloader/sapiens";
        private String baseUrl;
        //return "http://fr.wikipedia.org/wiki/";
        //return "";
    }
    
    @Bean
    public IModule module() {
        return new FindPhrasesModule();
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
