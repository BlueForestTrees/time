package time.append;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.TextAnalyser;
import time.analyser.finder.DatedPhrasesFinders;
import time.conf.Conf;
import time.domain.Text;
import time.storage.store.PhraseStore;
import time.tika.TextFactory;
import time.tool.string.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppendRun {

    private static final Logger LOGGER = LogManager.getLogger(DatedPhrasesFinders.class);

    private final TextFactory textFactory;
    private final String baseAppendIndexDir;
    private TextAnalyser textAnalyser;

    @Inject
	public AppendRun(final Conf conf, final TextFactory textFactory, final TextAnalyser textAnalyser) {
        this.textFactory = textFactory;
		this.textAnalyser = textAnalyser;
        this.baseAppendIndexDir = conf.getBaseAppendIndexDir();
	}

	public void run(final Append append) throws IOException {

		LOGGER.info("append {}", append);

		/*final Text text = textFactory.build(append.getUrl(), append.getTitle(), new String(Files.readAllBytes(Paths.get(append.getSource()))));
        textAnalyser.analyse(text);

        final String indexDir = Strings.withSlash(baseAppendIndexDir) + Strings.forFilename(append.getTitle());
        PhraseStore store = new PhraseStore(indexDir);

        store.start();
        store.store(text);
		store.stop();*/
	}

}
