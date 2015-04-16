package wiki;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import wiki.config.TransformerConfig;
import wiki.service.MapLiensService;


public class TransformerMapLiens {
	
	private static final Logger logger = LogManager.getLogger(TransformerMapLiens.class);
	
	static{
		System.setProperty("org.jboss.logging.provider", "log4j2");
	}
	
	public static void main(String[] args) throws IOException {
		logger.info("main");
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TransformerConfig.class)){
			MapLiensService mapLiensService = (MapLiensService)ctx.getBean(MapLiensService.class);
			mapLiensService.run();
		}
		logger.info("main end");
	}
}
