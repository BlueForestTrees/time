package time.conf;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PropFileInConf implements InConf{
	
	private static final Logger LOGGER = LogManager.getLogger(PropFileInConf.class);

	@Override
	public void appendTo(final Conf conf) throws RuntimeException {
		final String confFile = conf.getConfFile();
		
		LOGGER.info(confFile);
				
		try{
			Files.lines(Paths.get(confFile)).forEach(line -> addProp(line, conf));
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}


	@Override
	public int position() {
		return -10;
	}
	
	@Override
	public String toString(){
		return "Prop File";
	}

	private void addProp(final String line, final Conf conf) {
		final String key = line.split("=")[0];
		final String value = line.split("=")[1];
		conf.put(key, value);
	}
}
