package time.merger;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import time.merger.service.MergeIndexService;

@SpringBootApplication
public class MergerMain {
    private static final Log LOG = LogFactory.getLog(MergerMain.class);

    @Autowired
    private ApplicationArguments argz;
    
    @Autowired 
    private MergeIndexService mergeIndexService;
    
    @Bean
    protected String src(){
    	return argz.getOptionValues("src").get(0);
    }
    
    @Bean
    protected String dest(){
    	return argz.getOptionValues("dest").get(0);
    }
    
    public static void main(String... args) throws Exception {
        final ApplicationContext ctx = SpringApplication.run(MergerMain.class, args);
        ctx.getBean(MergerMain.class).execute();
    }

	private void execute() throws IOException {
		LOG.info("mergeIndexService.merge("+src()+","+dest()+")");
		mergeIndexService.merge(src(), dest());
	}
    


}
