package time.crawler.livre;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.tika.ToText;
import time.tool.file.Dirs;
import time.transformer.store.TextStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LivreRun {

	private static final Logger LOGGER = LogManager.getLogger(LivreRun.class);

	private final String sourceDir;
	private final ToText toText;
	private final TextStore store;

	@Inject
	public LivreRun(@Named("conf") final Conf conf, final TextStore store) {
		this.sourceDir = conf.getSourceDir();
		if (this.sourceDir == null) {
			throw new IllegalArgumentException("invalid sourceDir: " + sourceDir);
		}
		this.toText = new ToText();
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
		store.storeText(toText.from(input));
	}

}
