package time.dumper;

import org.apache.log4j.Logger;

import time.dumper.config.DumperConfig;
import time.dumper.controller.DumperController;
import time.dumper.factory.DumperFactory;

public class Dumper {
	static final Logger log = Logger.getLogger(Dumper.class);

	public static void main(String[] args) throws Exception {
		System.setProperty( "file.encoding", "UTF-8" );
		
		DumperFactory factory = new DumperFactory();
		DumperConfig config = factory.fromArguments(args);

		if (config.isHelp()) {
			log.info("configuration du crawler" + config.getConfAsString());
		} else {
			log.info("démarrage du crawler" + config.getConfAsString());
			
			DumperController dumpController = factory.buildController(config);
			dumpController.start();

			log.info("fin du crawler" + config.getConfAsString());
		}
	}

}
