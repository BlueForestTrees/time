package wiki.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan({"wiki.service"})
@Import(DbConfig.class)
public class ComponentConfig {
	
}
