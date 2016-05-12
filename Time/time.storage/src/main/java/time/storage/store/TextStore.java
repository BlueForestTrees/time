package time.storage.store;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import time.analyser.TextAnalyser;
import time.domain.Text;
import time.storage.filter.TextFilter;

public class TextStore {
	private static final Logger LOG = LogManager.getLogger(TextStore.class);
	private final PhraseStore storage;
	private TextFilter textFilter;
	private TextAnalyser textAnalyser;

	@Inject
	public TextStore(final TextAnalyser textAnalyser, final TextFilter textFilter,
					 final PhraseStore storage) {
		this.textAnalyser = textAnalyser;
		this.textFilter = textFilter;
		this.storage = storage;
	}

	public void start() {
		LOG.info("start");
		try {
			storage.start();
		} catch (IOException e) {
			throw new RuntimeException("TextStore.start throw ", e);
		}
	}

    public long storeText(final Text text) {
        if (textFilter.keep(text)) {
            textAnalyser.analyse(text).getPhrases().forEach(storage::store);
            return text.getPhrases().size();
        }
        return 0;
    }

	public void stop(){
		try {
			storage.end();
		} catch (IOException e) {
			throw new RuntimeException("TextStore.stop throw ", e);
		}
		LOG.info("run end");
	}

}
