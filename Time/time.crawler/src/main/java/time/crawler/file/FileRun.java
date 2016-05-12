package time.crawler.file;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import time.conf.Conf;
import time.storage.store.TextStore;
import time.tika.TextFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileRun {

	private final TextStore store;
	private final String source;
	private final String url;
	private final String title;
    private final TextFactory textFactory;

    @Inject
	public FileRun(@Named("conf") Conf conf, final TextStore store, final TextFactory textFactory) {
		this.source = conf.getSource();
		this.url = conf.getUrl();
		this.title = conf.getTitle();
		this.store = store;
        this.textFactory = textFactory;
	}

	public void run() throws IOException {
		store.start();

		store.storeText(textFactory.build(url, title, new String(Files.readAllBytes(Paths.get(source)))));

		store.stop();
	}

}
