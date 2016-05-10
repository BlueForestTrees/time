package time.transformer.store;

import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.DatedPhrase;
import time.repo.bean.Scale;
import time.repo.bean.SortableLongField;
import time.tool.reference.Fields;

public class PhraseStore {

	private static final Logger LOGGER = LogManager.getLogger(PhraseStore.class);

	private String indexDir;
	private IndexWriter iwriter;
	private FacetsConfig config;

	@Inject
	public PhraseStore(@Named("conf") Conf conf) {
		indexDir = conf.getIndexDir();
		LOGGER.info("index: " + indexDir);
		if (indexDir == null) {
			throw new RuntimeException("indexDir is null");
		}
	}

	public void start() throws IOException {
		final Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexDir));
		final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new FrenchAnalyzer());
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		indexWriterConfig.setRAMBufferSizeMB(256.0);

		iwriter = new IndexWriter(directory, indexWriterConfig);
		config = new FacetsConfig();
	}

	public void store(final DatedPhrase phrase) {
		final Document doc = new Document();
		doc.add(new TextField(Fields.TEXT, phrase.getText(), Store.YES));
		doc.add(new TextField(Fields.PAGE_URL, phrase.getPageUrl(), Store.YES));
		doc.add(new SortableLongField(Fields.DATE, phrase.getDate(), Store.YES));

		for (int i = 0; i < Scale.scales.length; i++) {
			doc.add(new LongField(String.valueOf(i), phrase.getDate() / Scale.scales[i], Store.NO));
			doc.add(new SortedSetDocValuesFacetField(String.valueOf(i),
					String.valueOf(phrase.getDate() / Scale.scales[i])));
		}

		try {
			iwriter.addDocument(config.build(doc));
		} catch (IOException e) {
			throw new RuntimeException("IndexWriter.addDocument error", e);
		}
	}

	public void end() throws IOException {
		iwriter.forceMerge(1);
		iwriter.close();
	}

}