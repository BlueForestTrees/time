package time.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import time.conf.Conf;
import time.conf.InConf;

@Component
public class ArgsInConf implements InConf {
	
	@Autowired
	private ApplicationArguments args;
	
	@Override
	public void appendTo(final Conf conf) {
		args.getOptionNames().forEach(key -> conf.put(key,  args.getOptionValues(key).get(0)));		
	}

	@Override
	public int position() {
		return -100;
	}
	
	@Override
	public String toString(){
		return "Args";
	}

}