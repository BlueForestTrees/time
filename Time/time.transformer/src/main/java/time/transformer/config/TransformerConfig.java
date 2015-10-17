package time.transformer.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import time.transformer.component.reader.SmartScanner;

@Configuration
@ComponentScan({"time.transformer.service", "time.transformer.component", "time.transformer.tool"})
@Import({TransformerDbConfig.class, DateFinderConfig.class})
public class TransformerConfig {
	@Bean
	public String path(){
		return "c:/time/data";
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
	public Long pageTotal(){
		return 3700000L;
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
