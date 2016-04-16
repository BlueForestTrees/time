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

import time.conf.Conf;
import time.crawler.write.IWriter;
import time.crawler.write.Write;
import time.tool.file.Dirs;

public class FileWriter implements IWriter {

	private static final Logger LOGGER = LogManager.getLogger(FileWriter.class);
	
	final File outputDir;

	private Conf conf;

	@Inject
	public FileWriter(@Named("conf") final Conf conf) throws IOException {
		this.conf = conf;
		outputDir = new File(conf.getTxtPagesDir());
		Dirs.renew(outputDir);
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

		try {
			Files.write(path, content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
