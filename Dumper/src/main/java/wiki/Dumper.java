package wiki;

import org.apache.log4j.Logger;

import wiki.config.DumperConfig;
import wiki.controller.DumperController;
import wiki.factory.DumperFactory;

public class Dumper {
	static final Logger log = Logger.getLogger(Dumper.class);

	public static void main(String[] args) throws Exception {
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
