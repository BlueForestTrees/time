package time.local.tika;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Conf;
import time.domain.Metadata;
import time.domain.Text;
import time.messaging.Messager;
import time.messaging.Queue;
import time.storage.store.TextHandler;
import time.tika.TextFactory;
import time.tool.file.Dirs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class FilesRun {

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
	}

	private void writeFile(final File source) {
		LOGGER.debug(source);
		FileInputStream input;
		try {
			input = new FileInputStream(source);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Lecture fichier", e);
		}
		final Text text = textFactory.build(input);
		text.getMetadata().setType(Metadata.Type.FILE);
		store.handleText(text);
	}

}
