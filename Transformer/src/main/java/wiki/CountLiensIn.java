package wiki;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import wiki.config.TransformerConfig;
import wiki.service.CountLiensService;
import wiki.service.Runner;


public class CountLiensIn {
	
	private static final Logger logger = LogManager.getLogger(CountLiensIn.class);
	
	public static void main(String[] args) throws IOException {
		logger.info("main");
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TransformerConfig.class)){
			Runner runner = ctx.getBean(Runner.class);	
			CountLiensService service = ctx.getBean(CountLiensService.class);
			runner.setService(service);
			runner.run();
		}
		logger.info("main end");
	}
}
