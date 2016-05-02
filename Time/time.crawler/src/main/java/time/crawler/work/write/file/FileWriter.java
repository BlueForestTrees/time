package time.crawler.work.write.file;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.crawler.work.write.Write;
import time.crawler.work.write.IWriter;
import time.repo.bean.Text;
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

	public void writePage(final Text text) {
		final String txtOutputDir = conf.getTxtOutputDir();
		final String filename = Dirs.filenameAble(text.getTitle());
		final String filepath = txtOutputDir + filename;
		if(txtOutputDir == null || filename == null){
			LOGGER.error("invalid path: " + filepath);
			return;
		}
		final Path path = Paths.get(filepath);
		final byte[] content = Write.concat(text.getUrl(), text.getMetadata(), text.getTextString()).toString().getBytes();

		try {
			Files.write(path, content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
