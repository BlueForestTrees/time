package time.crawler.write.file;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.crawler.write.IWriter;
import time.crawler.write.Write;
import time.repo.bean.Page;
import time.tool.file.Dirs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter implements IWriter {

	private static final Logger LOGGER = LogManager.getLogger(FileWriter.class);
	
	final File outputDir;

	private Conf conf;

	@Inject
	public FileWriter(@Named("conf") final Conf conf) throws IOException {
		this.conf = conf;
		outputDir = new File(conf.getTxtOutputDir());
		Dirs.renew(outputDir);
	}

	public void writePage(final Page page) {
		final String txtPagesDir = conf.getTxtOutputDir();
		final String filename = Dirs.filenameAble(page.getTitle());
		final String filepath = txtPagesDir + filename;
		if(txtPagesDir == null || filename == null){
			LOGGER.error("invalid path: " + filepath);
			return;
		}
		final Path path = Paths.get(filepath);
		final byte[] content = Write.concat(page.getUrl(), page.getMetadata(), page.getTextString()).toString().getBytes();

		try {
			Files.write(path, content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
