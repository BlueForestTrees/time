package time.crawler.file;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import time.conf.Conf;
import time.domain.Text;
import time.storage.store.TextStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileRun {

	private final TextStore store;
	private final String source;
	private final String url;
	private final String title;

	@Inject
	public FileRun(@Named("conf") Conf conf, final TextStore store) {
		this.source = conf.getSource();
		this.url = conf.getUrl();
		this.title = conf.getTitle();
		this.store = store;
	}

	public void run() throws IOException {
		store.start();

		final Text text = new Text();
		text.setText(new String(Files.readAllBytes(Paths.get(source))));
		text.setUrl(url);
		text.setTitle(title);
		
		store.storeText(text);

		store.stop();
	}

}
