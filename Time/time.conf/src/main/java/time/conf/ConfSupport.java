package time.conf;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfSupport {
	
    private static final Logger LOGGER = LogManager.getLogger(ConfSupport.class);

	@Autowired
	private List<InConf> appenders;

	@Bean
	public Conf conf() {
		final Conf conf = new Conf();
		
		appenders.sort((c1, c2) -> Integer.compare(c1.position(), c2.position()));
		LOGGER.info(appenders);
		
		appenders.forEach(cp -> cp.appendTo(conf));
		
		return conf;
	}

	public enum Envars {
		home, target
	}

}
