package wiki;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import wiki.config.TransformerConfig;
import wiki.service.UrlFromFileToBaseService;


public class TransformerFileToUrl {
	
	private static final Logger logger = LogManager.getLogger(UrlFromFileToBaseService.class);
	
	public static void main(String[] args) throws IOException {
		logger.info("main");
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TransformerConfig.class)){
			UrlFromFileToBaseService transformService = (UrlFromFileToBaseService)ctx.getBean(UrlFromFileToBaseService.class);
			transformService.run();
		}
		logger.info("main end");
	}
}
