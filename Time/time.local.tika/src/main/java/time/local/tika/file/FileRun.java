package time.local.tika.file;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.finder.DatedPhrasesFinders;
import time.conf.Conf;
import time.storage.store.TextHandler;
import time.tika.TextFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileRun {

    private static final Logger LOGGER = LogManager.getLogger(DatedPhrasesFinders.class);

	private final TextHandler store;
    private final TextFactory textFactory;

    @Inject
	public FileRun(final TextHandler store, final TextFactory textFactory) {
		this.store = store;
        this.textFactory = textFactory;
	}

	public void run(final Append append) throws IOException {

		LOGGER.info("append {}", append);

		store.start();

		store.handleText(textFactory.build(append.getUrl(), append.getTitle(), new String(Files.readAllBytes(Paths.get(append.getSource())))));

		store.stop();
	}

}
