package time.storage.store;

import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import time.domain.DatedPhrase;
import time.domain.Scale;
import time.domain.SortableLongField;
import time.domain.Text;
import time.tool.file.Dirs;
import time.tool.reference.Fields;

public class PhraseStore {

	private static final Logger LOGGER = LogManager.getLogger(PhraseStore.class);

	private String indexDir;
    private String finalIndexDir;
	private IndexWriter iwriter;
	private FacetsConfig config;
    private long storedPhraseCount;
    private long nbPhraseLog;

	@Inject
	public PhraseStore(@Named("conf") Conf conf) {
		indexDir = conf.getIndexDir();
        finalIndexDir = conf.getFinalIndexDir();
        nbPhraseLog = conf.getNbPhraseLog();
		if (indexDir == null) {
			throw new RuntimeException("indexDir is null");
		}
        LOGGER.info(this);
	}

	public void start() throws IOException {
		final Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexDir));
		final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new FrenchAnalyzer());
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		indexWriterConfig.setRAMBufferSizeMB(256.0);

		iwriter = new IndexWriter(directory, indexWriterConfig);
		config = new FacetsConfig();
        storedPhraseCount = 0;
	}

	public void store(final Text text) {
        text.getPhrases().forEach(phrase -> this.storePhrase(text, phrase));
	}

    private void storePhrase(final Text text, final DatedPhrase phrase) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug(phrase.getUrl() + " " + phrase.getText());
        }
        handleNulls(text, phrase);
        final Document doc = createDoc(text, phrase);
        addDoc(doc);
        logProgress();
    }

    private void handleNulls(Text text, DatedPhrase phrase) {
        if(text.getUrl() == null){
            throw new RuntimeException("PhraseStore.storePhrase : text.getUrl() == null");
        }
        if(phrase.getText() == null){
            throw new RuntimeException("PhraseStore.storePhrase : phrase.getText() == null");
        }
    }

    private Document createDoc(Text text, DatedPhrase phrase) {
        final Document doc = new Document();
        if(text.getTitle() != null){
            doc.add(new TextField(Fields.TITLE, text.getTitle(), Store.YES));
        }
        if(text.getCreator() != null){
            doc.add(new TextField(Fields.AUTHOR, text.getCreator(), Store.YES));
        }
        doc.add(new TextField(Fields.TEXT, phrase.getText(), Store.YES));
        doc.add(new TextField(Fields.URL, text.getUrl(), Store.YES));
        doc.add(new SortableLongField(Fields.DATE, phrase.getDate(), Store.YES));
        for (int i = 0; i < Scale.scales.length; i++) {
            doc.add(new LongField(String.valueOf(i), phrase.getDate() / Scale.scales[i], Store.NO));
            doc.add(new SortedSetDocValuesFacetField(String.valueOf(i),
                    String.valueOf(phrase.getDate() / Scale.scales[i])));
        }
        return doc;
    }

    private void addDoc(Document doc) {
        try {
            iwriter.addDocument(config.build(doc));
            storedPhraseCount++;
        } catch (IOException e) {
            throw new RuntimeException("IndexWriter.addDocument error", e);
        }
    }

    private void logProgress() {
        if(storedPhraseCount%nbPhraseLog == 0){
            LOGGER.info(storedPhraseCount + " phrases stored.");
        }
    }

    public void stop() throws IOException {
        LOGGER.info("stop (merge & close index), " + storedPhraseCount + " documents in index");
		iwriter.forceMerge(1);
		iwriter.close();
        if(finalIndexDir != null){
            LOGGER.info("move Index from " + indexDir + " to " + finalIndexDir);
            Dirs.move(indexDir, finalIndexDir);
        }else{
            LOGGER.info("no move since no finalIndexDir");
        }
	}

    @Override
    public String toString() {
        return "PhraseStore{" +
                "indexDir='" + indexDir + '\'' +
                ", finalIndexDir='" + finalIndexDir + '\'' +
                ", iwriter=" + iwriter +
                ", config=" + config +
                '}';
    }
}
