package time.storage.store;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import time.analyser.TextAnalyser;
import time.domain.Text;
import time.storage.filter.TextFilter;

public class TextHandler {
	private static final Logger LOGGER = LogManager.getLogger(TextHandler.class);
	private final PhraseStore phraseStore;
	private TextFilter textFilter;
	private TextAnalyser textAnalyser;

	@Inject
	public TextHandler(final TextAnalyser textAnalyser,
					   final TextFilter textFilter,
					   final PhraseStore phraseStore) {
		this.textAnalyser = textAnalyser;
		this.textFilter = textFilter;
		this.phraseStore = phraseStore;
		LOGGER.info(this);
	}

	public void start() {
		try {
			phraseStore.start();
		} catch (IOException e) {
			throw new RuntimeException("TextHandler.start throw ", e);
		}
	}

    public long handleText(final Text text) {
		if (textFilter.keep(text)) {
            textAnalyser.analyse(text);
			phraseStore.store(text);
			return text.getPhrases().size();
        }
        return 0;
    }

	public void stop(){
		try {
			phraseStore.stop();
		} catch (IOException e) {
			throw new RuntimeException("TextHandler.stop throw ", e);
		}
	}

	@Override
	public String toString() {
		return "TextHandler{" +
				"phraseStore=" + phraseStore +
				", textFilter=" + textFilter +
				", textAnalyser=" + textAnalyser +
				'}';
	}
}
