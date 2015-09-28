package wiki;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import wiki.config.TransformerConfig;
import wiki.service.FindUrlsService;
import wiki.service.Runner;


public class FindUrls {
	
	private static final Logger logger = LogManager.getLogger(FindUrlsService.class);
	
	public static void main(String[] args) throws IOException {
		logger.info("main");
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TransformerConfig.class)){
			Runner runner = ctx.getBean(Runner.class);	
			//FindUrlsService service = ctx.getBean(FindUrlsService.class);
			//runner.setService(service);
			runner.run();
		}
		logger.info("main end");
	}
}
