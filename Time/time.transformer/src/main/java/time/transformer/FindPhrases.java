
package time.transformer;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import time.transformer.config.FindPhrasesConfig;
import time.transformer.service.Runner;

public class FindPhrases {

private static final Logger logger = LogManager.getLogger(FindPhrases.class);
	
	public static void main(String[] args) throws IOException {
		logger.info("main");
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(FindPhrasesConfig.class)){
			((Runner)ctx.getBean(Runner.class)).run();
		}
		logger.info("main end");
	}

}