package time.conf;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import time.conf.ConfSupport.Envars;

@Component
public class EnvVarsInConf implements InConf {

	@Override
	public void appendTo(Conf conf) {
		Arrays.stream(Envars.values())
		.map(envar -> envar.toString())
		.forEach(envar -> conf.put(envar, System.getenv(envar)));	
	}

	@Override
	public int position() {
		return -1000;
	}
	
	@Override
	public String toString(){
		return "Env Vars";
	}

}
