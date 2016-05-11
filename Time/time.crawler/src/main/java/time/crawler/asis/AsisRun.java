package time.crawler.asis;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import time.conf.Conf;
import time.repo.bean.Text;
import time.transformer.store.TextStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AsisRun {

	private final TextStore store;
	private Conf conf;

	@Inject
	public AsisRun(@Named("conf") Conf conf, final TextStore store) {
		this.conf = conf;
		this.store = store;
	}

	public void run() throws IOException {
		store.start();

		final Text text = new Text();
		text.setText(new String(Files.readAllBytes(Paths.get(conf.getSourceDir() + conf.getSource()))));
		text.setUrl(conf.getUrl());
		text.setTitle(conf.getTitle());
		
		store.storeText(text);

		store.stop();
	}

}
