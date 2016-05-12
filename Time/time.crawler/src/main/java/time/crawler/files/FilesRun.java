package time.crawler.files;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.tika.TextFactory;
import time.tool.file.Dirs;
import time.storage.store.TextStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FilesRun {

	private static final Logger LOGGER = LogManager.getLogger(FilesRun.class);

	private final String sourceDir;
	private final TextFactory textFactory;
	private final TextStore store;

	@Inject
	public FilesRun(@Named("conf") final Conf conf, final TextStore store) {
		this.sourceDir = conf.getSourceDir();
		if (this.sourceDir == null) {
			throw new IllegalArgumentException("invalid sourceDir: " + sourceDir);
		}
		this.textFactory = new TextFactory();
		this.store = store;
	}

	public void run() {
		store.start();
		Dirs.files(sourceDir).forEach(this::writeFile);
		store.stop();
	}

	private void writeFile(final File source) {
		LOGGER.info(source);
		FileInputStream input;
		try {
			input = new FileInputStream(source);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Lecture fichier", e);
		}
		store.storeText(textFactory.build(input));
	}

}
