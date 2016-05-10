package time.transformer.read2store;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;

import time.analyser.analyse.TextAnalyser;
import time.repo.bean.Text;
import time.transformer.filter.TextFilter;
import time.transformer.transform.ITextTransformer;
import time.transformer.store.PhraseStore;

public class TextStore {
	private static final Logger LOG = LogManager.getLogger(TextStore.class);
	private final PhraseStore storage;
	private TextFilter textFilter;
	private ITextTransformer transformer;
	private TextAnalyser textAnalyser;

	@Inject
	public TextStore(final TextAnalyser textAnalyser,
                     final TextFilter textFilter, final ITextTransformer transformer, final PhraseStore storage) {
		this.textAnalyser = textAnalyser;
		this.textFilter = textFilter;
		this.transformer = transformer;
		this.storage = storage;
	}

	public void start() {
		LOG.info("start");
		try {
			storage.start();
		} catch (IOException e) {
			throw new RuntimeException("TextAnalyser.start throw ", e);
		}
	}

    public long storeText(final Text text) throws IOException {
        if (textFilter.keep(text)) {
            transformer.transform(text);
            textAnalyser.analyse(text);
            text.getPhrases().forEach(storage::store);
            return text.nbDatedPhrasesCount();
        }
        return 0;
    }

	public void stop(){
		try {
			storage.end();
		} catch (IOException e) {
			throw new RuntimeException("TextAnalyser.end throw ", e);
		}
		LOG.info("run end");
	}

}
