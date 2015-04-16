package wiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value={CrawlConfig.class, CrawlersConfig.class, ComponentConfig.class})
public class AppConfig {
	
}
