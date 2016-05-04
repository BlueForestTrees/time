package time.transformer.read2store;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.analyser.analyse.TextAnalyser;
import time.conf.Conf;
import time.repo.bean.Text;
import time.tool.chrono.Chrono;
import time.transformer.filter.TextFilter;
import time.transformer.transform.ITextTransformer;
import time.transformer.read.FinDuScanException;
import time.transformer.read.TextReader;
import time.transformer.store.LuceneStorage;

/**
 * Traite toutes les données par packets avec des logs intermédiaire. Compte le
 * nombre de pages traitées.
 * 
 * @author slim
 *
 */
public class TextReaderToStoreService {
	private static final Logger LOG = LogManager.getLogger(TextReaderToStoreService.class);
	private final LuceneStorage storage;
	private Long pageSize;
	private long pageTotal;
	private long maxPhrasesToFetch;
	private TextReader textReader;
	private TextFilter textFilter;
	private ITextTransformer pageTransformer;
	private TextAnalyser textAnalyser;

	@Inject
	public TextReaderToStoreService(@Named("conf") Conf conf, final TextAnalyser textAnalyser, TextReader textReader,
									TextFilter textFilter, ITextTransformer pageTransformer, LuceneStorage storage) {
		this.pageSize = conf.getPageSize();
		this.pageTotal = conf.getPageTotal();
		this.maxPhrasesToFetch = conf.getMaxPhrasesToFetch();
		this.textAnalyser = textAnalyser;
		this.textReader = textReader;
		this.textFilter = textFilter;
		this.pageTransformer = pageTransformer;
		this.storage = storage;
	}

	public void start() {
		LOG.info("start");
		try {
			storage.start();
		} catch (IOException e) {
			throw new RuntimeException("TextAnalyser.start throw ", e);
		}
		handlePages();
		try {
			storage.end();
		} catch (IOException e) {
			throw new RuntimeException("TextAnalyser.end throw ", e);
		}
		LOG.info("run end");
	}

	private void handlePages(){
		long pageCount = 0;
		long phraseCount = 0;
		final Chrono chrono = new Chrono("Text");
		final Chrono fullChrono = new Chrono("Full");

		fullChrono.start();
		try {
			do {
				chrono.start();
				phraseCount += handlePages(pageSize);
				pageCount += pageSize;
				chrono.stop();
				fullChrono.stop();
				LOG.debug("#" + pageCount + ", Total:" + fullChrono + ", Moy:" + fullChrono.toStringDividedBy(pageSize) + ", last:" + chrono + ", reste:" + fullChrono.getRemaining(pageCount, pageTotal) + " phrase#" + phraseCount);
			} while (maxPhrasesToFetch == -1L || (phraseCount < maxPhrasesToFetch));
		} catch (FinDuScanException e) {
			LOG.info("fin du scan (" + pageCount + " pages, " + phraseCount + " phrases");
		} catch (IOException e){
			throw new RuntimeException("textReader throw ", e);
		}
	}

	public long handlePages(long pageCount) throws IOException, FinDuScanException {
		LOG.info("run " + pageCount);
		long phraseCount = 0;
		for (long i = 0; i < pageCount; i++) {
			final Text text = textReader.getNextText();
			if (textFilter.keep(text)) {
				pageTransformer.transform(text);
				textAnalyser.analyse(text);
				text.getPhrases().forEach(storage::store);
				phraseCount += text.nbDatedPhrasesCount();
			}
		}
		return phraseCount;
	}

}
