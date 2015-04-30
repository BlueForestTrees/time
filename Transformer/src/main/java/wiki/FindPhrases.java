package wiki;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import wiki.config.TransformerConfig;
import wiki.service.FindPhrasesService;
import wiki.service.IService;
import wiki.service.Runner;

public class FindPhrases {

private static final Logger logger = LogManager.getLogger(CountLiensIn.class);
	
	public static void main(String[] args) throws IOException {
		logger.info("main");
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TransformerConfig.class)){
			Runner runner = ctx.getBean(Runner.class);	
			IService service = ctx.getBean(FindPhrasesService.class);
			runner.setService(service);
			runner.run();
		}
		logger.info("main end");
	}

}
