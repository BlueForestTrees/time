package time.crawler.asis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.conf.Conf;
import time.crawler.write.IWriter;

@Component
public class Asisman {
	
	@Autowired
	public IWriter writer;
	
	@Autowired
	private Conf conf;

	public void go() throws IOException {
		final String source = conf.getSourceDir() + conf.asstring("source");
		final String text = new String(Files.readAllBytes(Paths.get(source)));
		final String url = conf.asstring("url");
		final String title = conf.asstring("title");
		
		writer.writePage(url, title, text);
	}

}
