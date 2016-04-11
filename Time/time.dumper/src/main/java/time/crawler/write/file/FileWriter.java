package time.crawler.write.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.conf.Conf;
import time.crawler.write.IWriter;
import time.crawler.write.Write;
import time.tool.file.Dirs;

@Component
public class FileWriter implements IWriter {

    @Autowired
    private Conf conf;
	
	@Override
	public void writePage(final String url, final String title, final String text) {
		final Path path = Paths.get(conf.getTxtPagesDir() + Dirs.filenameAble(title));
		final byte[] content = Write.concat(url, title, text).toString().getBytes();
		
		try {
			Files.write(path, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
