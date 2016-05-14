package time.storage.store;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import time.analyser.TextAnalyser;
import time.domain.Text;
import time.storage.filter.TextFilter;

public class TextStore {
	private static final Logger LOGGER = LogManager.getLogger(TextStore.class);
	private final PhraseStore phraseStore;
	private TextFilter textFilter;
	private TextAnalyser textAnalyser;

	@Inject
	public TextStore(final TextAnalyser textAnalyser,
					 final TextFilter textFilter,
					 final PhraseStore phraseStore) {
		this.textAnalyser = textAnalyser;
		this.textFilter = textFilter;
		this.phraseStore = phraseStore;
		LOGGER.info(this);
	}

	public void start() {
		LOGGER.info("TextStore.start()");
		try {
			phraseStore.start();
		} catch (IOException e) {
			throw new RuntimeException("TextStore.start throw ", e);
		}
	}

    public long storeText(final Text text) {
		if (textFilter.keep(text)) {
            textAnalyser.analyse(text).getPhrases().forEach(phraseStore::store);
            return text.getPhrases().size();
        }
        return 0;
    }

	public void stop(){
		LOGGER.info("stop");
		try {
			phraseStore.stop();
		} catch (IOException e) {
			throw new RuntimeException("TextStore.stop throw ", e);
		}
	}

	@Override
	public String toString() {
		return "TextStore{" +
				"phraseStore=" + phraseStore +
				", textFilter=" + textFilter +
				", textAnalyser=" + textAnalyser +
				'}';
	}
}
