package time.transformer;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import time.transformer.config.TransformerConfig;

public class Transformer {
    
    private static final Logger LOG = LogManager.getLogger(Transformer.class);
    
    private Transformer(){
        
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("file.encoding", "UTF-8");

        LOG.info("Transformer start");
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TransformerConfig.class)) {
            ((Runner) ctx.getBean(Runner.class)).run();
        }
        LOG.info("Transformer end");
    }

}
