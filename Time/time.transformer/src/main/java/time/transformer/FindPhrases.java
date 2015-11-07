package time.transformer;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import time.transformer.config.FindPhrasesConfig;
import time.transformer.service.Runner;

public class FindPhrases {
    
    private static final Logger LOG = LogManager.getLogger(FindPhrases.class);
    
    private FindPhrases(){
        
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("file.encoding", "UTF-8");

        LOG.info("FindPhrases start");
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(FindPhrasesConfig.class)) {
            ((Runner) ctx.getBean(Runner.class)).run();
        }
        LOG.info("FindPhrases end");
    }

}
