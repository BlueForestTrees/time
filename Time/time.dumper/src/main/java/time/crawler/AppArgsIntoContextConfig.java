package time.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import time.tool.conf.Context;

@Configuration
public class AppArgsIntoContextConfig {
	
	@Autowired
	private ApplicationArguments args;

	@Bean
	public Context context() {
		//final String basePath = args.getOptionValues(ConfKeys.basePath).get(0);
		//final String name = args.getOptionValues(ConfKeys.name).get(0);
		final Context context = new Context();
		args.getOptionNames().forEach(key -> context.put(key,  args.getOptionValues(key).get(0)));
		return context;
	}

}
