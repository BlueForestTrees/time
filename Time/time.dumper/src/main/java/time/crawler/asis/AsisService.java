package time.crawler.asis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.inject.Inject;

import time.crawler.conf.Conf;
import time.crawler.write.IWriter;

public class AsisService {
	
	public IWriter writer;
	
	private Conf conf;

	@Inject
	public AsisService(IWriter writer, Conf conf) {
		this.writer = writer;
		this.conf = conf;
	}

	public void run() throws IOException {
		final String source = conf.getSourceDir() + conf.getSource();
		final String text = new String(Files.readAllBytes(Paths.get(source)));
		final String url = conf.getUrl();
		final String title = conf.getTitle();
		
		writer.writePage(url, title, null, text);
	}

}
