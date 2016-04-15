package time.crawler.write.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.crawler.conf.Conf;
import time.crawler.write.IWriter;
import time.crawler.write.Write;
import time.tool.file.Dirs;

public class FileWriter implements IWriter {

	private static final Logger LOGGER = LogManager.getLogger(FileWriter.class);

	private Conf conf;

	@Inject
	public FileWriter(@Named("conf") final Conf conf) {
		this.conf = conf;
		new File(conf.getTxtPagesDir()).mkdirs();
	}

	public void writePage(final String url, final String title, final String metadata, final String text) {
		final String txtPagesDir = conf.getTxtPagesDir();
		final String filename = Dirs.filenameAble(title);
		final String filepath = txtPagesDir + filename;
		if(txtPagesDir == null || filename == null){
			LOGGER.error("invalid path: " + filepath);
			return;
		}
		final Path path = Paths.get(filepath);
		final byte[] content = Write.concat(url, metadata, text).toString().getBytes();

		LOGGER.info(path);

		try {
			Files.write(path, content);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

}
