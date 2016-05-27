package time.local.tika.files;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.messaging.Messager;
import time.messaging.Queue;
import time.storage.store.TextHandler;
import time.tika.TextFactory;
import time.tool.file.Dirs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FilesRun {

	private static final Logger LOGGER = LogManager.getLogger(FilesRun.class);

	private final String sourceDir;
	private final TextFactory textFactory;
	private final TextHandler store;

	@Inject
	public FilesRun(final Conf conf, final TextHandler store, final TextFactory textFactory) {
		this.sourceDir = conf.getSourceDir();
		if (this.sourceDir == null) {
			throw new IllegalArgumentException("invalid sourceDir: {}" + sourceDir);
		}
		this.textFactory = textFactory;
		this.store = store;
	}

	public void run() {
		store.start();
		Dirs.files(sourceDir).forEach(this::writeFile);
		store.stop();
		try {
			new Messager().signal(Queue.MERGE);
		} catch (IOException | TimeoutException e) {
			LOGGER.error(e);
		}
	}

	private void writeFile(final File source) {
		LOGGER.info(source);
		FileInputStream input;
		try {
			input = new FileInputStream(source);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Lecture fichier", e);
		}
		store.handleText(textFactory.build(input));
	}

}
