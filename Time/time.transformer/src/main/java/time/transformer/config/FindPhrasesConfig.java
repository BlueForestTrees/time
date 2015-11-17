package time.transformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import time.transformer.service.FindPhrasesModule;
import time.transformer.service.IModule;

@Configuration
@Import({ TransformerConfig.class })
public class FindPhrasesConfig {

    @Bean
    public String indexPath() {
        return "/Time/data/page-phrases";
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
