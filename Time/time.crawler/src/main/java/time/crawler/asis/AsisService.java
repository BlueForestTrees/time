package time.crawler.asis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.inject.Inject;

import time.conf.Conf;
import time.crawler.work.write.IWriter;
import time.repo.bean.Text;

public class AsisService {
	
	public IWriter writer;
	
	private Conf conf;

	@Inject
	public AsisService(IWriter writer, Conf conf) {
		this.writer = writer;
		this.conf = conf;
	}

	public void run() throws IOException {
		final Text text = new Text();
		text.setText(new String(Files.readAllBytes(Paths.get(conf.getSourceDir() + conf.getSource()))));
		text.setUrl(conf.getUrl());
		text.setTitle(conf.getTitle());
		
		writer.writePage(text);
	}

}
