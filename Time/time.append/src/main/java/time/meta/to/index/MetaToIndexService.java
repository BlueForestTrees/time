package time.meta.to.index;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.TextAnalyser;
import time.domain.Meta;
import time.domain.IndexCreation;
import time.domain.Conf;
import time.domain.Text;
import time.storage.store.PhraseStore;
import time.tika.TextFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static time.tool.string.Strings.*;

public class MetaToIndexService {

    private static final Logger LOGGER = LogManager.getLogger(MetaToIndexService.class);

    private final TextFactory textFactory;
    private final String indexDir;
    private TextAnalyser textAnalyser;

    @Inject
	public MetaToIndexService(final Conf conf, final TextFactory textFactory, final TextAnalyser textAnalyser) {
        this.textFactory = textFactory;
		this.textAnalyser = textAnalyser;
        this.indexDir = conf.getIndexDir();
	}

    /**
     * Create Index from meta file
     * @param meta
     * @return
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
	public IndexCreation run(final Meta meta) throws IOException, InvocationTargetException, IllegalAccessException {

		LOGGER.info("meta {}", meta);

        //ANALYSE
        final Text text = textFactory.fromMetaPath(meta.getMetaPath());
        textAnalyser.analyse(text);

        //STORE
        final String indexDir = withSlash(this.indexDir) + forFilename(text.getMetadata().getTitre());
        final boolean overwrite = Files.isDirectory(Paths.get(indexDir));
        PhraseStore store = new PhraseStore(indexDir);

        store.start();
        store.store(text);
        final long phraseCount = store.stop();

        //RESPONSE
        final IndexCreation indexCreation = new IndexCreation();
        indexCreation.setSourceIndexDir(indexDir);
        indexCreation.setPhraseCount(phraseCount);
        indexCreation.setOverwriteOccurs(overwrite);
        return indexCreation;
    }
}
