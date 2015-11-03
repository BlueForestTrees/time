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
    public IModule module() {
        return new FindPhrasesModule();
    }

    @Bean
    public Long pageSize() {
        return 100L;
    }
}
