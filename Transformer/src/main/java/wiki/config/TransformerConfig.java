package wiki.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import wiki.component.reader.SmartScanner;

@Configuration
@ComponentScan({"wiki.service", "wiki.component", "wiki.tool"})
@Import(TransformerDbConfig.class)
public class TransformerConfig {
	
	@Bean
	public String path(){
		return "h:/histoire/pages";
	}
	
	@Bean
	public String baseUrl(){
		return "http://fr.wikipedia.org/wiki/";
	}
	
	@Bean
	public int urlMaxLength(){
		return 255;
	}
	
	@Bean
	public Long pageSize(){
		return 5000L;
	}
	
	@Bean
	public Long pageTotal(){
		return 1800000L;
	}
	
	@Bean String sep(){
		return "\\|¨";
	}
	
	@Bean
	public SmartScanner scanner() throws IOException{
		SmartScanner scanner = new SmartScanner(path());
		scanner.setDelimiter(sep());
		return scanner;
	}
	
}
