package time.crawler.livre;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.crawler.work.write.IWriter;
import time.tika.ToText;
import time.tool.file.Dirs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Livreman {

	private static final Logger LOGGER = LogManager.getLogger(Livreman.class);

	public IWriter writer;

	private Conf conf;
	private ToText toText;

	@Inject
	public Livreman(final IWriter writer, @Named("conf") final Conf conf) {
		this.writer = writer;
		this.conf = conf;
		this.toText = new ToText();
	}

	public void run() {
		final String sourceDir = conf.getSourceDir();
		if (sourceDir == null) {
			throw new IllegalArgumentException("invalid sourceDir: " + sourceDir);
		}
		Dirs.files(sourceDir).forEach(this::writePage);
	}

	/**
	 * Convertit une source epub/pdf en page.
	 *
	 * @param source
	 */
	private void writePage(final File source) {
		LOGGER.info(source);
		FileInputStream input;
		try {
			input = new FileInputStream(source);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Lecture fichier", e);
		}
		writer.writePage(toText.from(input));
	}

}
