package time.local.tika;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Conf;
import time.domain.Metadata;
import time.domain.Text;
import time.storage.store.TextHandler;
import time.tika.TextFactory;
import time.tool.file.Dirs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

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
		Dirs.files(sourceDir).filter(file -> !file.getName().endsWith(Metadata.EXT)).forEach(this::writeFile);
		store.stop();
	}

	private void writeFile(final File file) {
        final Text text;
        try {
            text = textFactory.fromFileMaybeMeta(file);
        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("fromFileMaybeMeta", e);
            return;
        }
        store.handleText(text);
	}

}
