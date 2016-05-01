package time.transformer;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.Text;
import time.tool.chrono.Chrono;
import time.transformer.page.filter.PageFilter;
import time.transformer.page.transformer.IPageTransformer;
import time.transformer.reader.FinDuScanException;
import time.transformer.reader.PageReader;
import time.transformer.storage.LuceneStorage;

/**
 * Traite toutes les données par packets avec des logs intermédiaire. Compte le
 * nombre de pages traitées.
 * 
 * @author slim
 *
 */
public class TransformerTxtSepBatch {
	private static final Logger LOG = LogManager.getLogger(TransformerTxtSepBatch.class);
	private final LuceneStorage storage;
	private Long pageSize;
	private long pageTotal;
	private long maxPhrasesToFetch;
	private PageReader pageReader;
	private PageFilter pageFilter;
	private IPageTransformer pageTransformer;
	private TextParser textParser;

	@Inject
	public TransformerTxtSepBatch(@Named("conf") Conf conf, final TextParser textParser, PageReader pageReader,
								  PageFilter pageFilter, IPageTransformer pageTransformer, LuceneStorage storage) {
		this.pageSize = conf.getPageSize();
		this.pageTotal = conf.getPageTotal();
		this.maxPhrasesToFetch = conf.getMaxPhrasesToFetch();
		this.textParser = textParser;
		this.pageReader = pageReader;
		this.pageFilter = pageFilter;
		this.pageTransformer = pageTransformer;
		this.storage = storage;
	}

	public void start() {
		LOG.info("start");
		try {
			storage.start();
		} catch (IOException e) {
			throw new RuntimeException("TextParser.start throw ", e);
		}
		doitBaby();
		try {
			storage.end();
		} catch (IOException e) {
			throw new RuntimeException("TextParser.end throw ", e);
		}
		LOG.info("run end");
	}

	private void doitBaby(){
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
			throw new RuntimeException("pageReader throw ", e);
		}
	}

	public long handlePages(long pageCount) throws IOException, FinDuScanException {
		LOG.info("run " + pageCount);
		long phraseCount = 0;
		for (long i = 0; i < pageCount; i++) {
			final Text text = pageReader.getNextPage();
			if (pageFilter.isValidPage(text) && pageFilter.isNewPage(text)) {
				pageTransformer.transform(text);
				textParser.parseText(text);
				text.getPhrases().forEach(storage::store);
				phraseCount += text.nbDatedPhrasesCount();
				pageFilter.rememberThisPage(text);
			}
		}
		return phraseCount;
	}

}
