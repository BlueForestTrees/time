package wiki.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan({"wiki.service","wiki.factory"})
@Import(DbConfig.class)
public class ComponentConfig {
	
}
