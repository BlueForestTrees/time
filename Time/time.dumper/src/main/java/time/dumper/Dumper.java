package time.dumper;

import org.apache.log4j.Logger;

import time.dumper.config.DumperConfig;
import time.dumper.controller.DumperController;
import time.dumper.factory.DumperFactory;

public class Dumper {
    private static final Logger LOGGER = Logger.getLogger(Dumper.class);

    private Dumper(){
        
    }
    
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");

        DumperFactory factory = new DumperFactory();
        DumperConfig config = factory.fromArguments(args);

        if (config.isHelp()) {
            LOGGER.info("configuration du crawler" + config.getConfAsString());
        } else {
            LOGGER.info("démarrage du crawler" + config.getConfAsString());

            DumperController dumpController = factory.buildController();
            dumpController.start();

            LOGGER.info("fin du crawler" + config.getConfAsString());
        }
    }

}
