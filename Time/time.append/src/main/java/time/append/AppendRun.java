package time.append;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.TextAnalyser;
import time.conf.Resolver;
import time.domain.Append;
import time.domain.AppendDone;
import time.domain.Conf;
import time.domain.Text;
import time.storage.store.PhraseStore;
import time.tika.TextFactory;
import time.tool.string.Strings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static time.tool.string.Strings.*;

public class AppendRun {

    private static final Logger LOGGER = LogManager.getLogger(AppendRun.class);

    private final TextFactory textFactory;
    private final String appendBaseIndexDir;
    private final String appendToDir;
    private TextAnalyser textAnalyser;

    @Inject
	public AppendRun(final Conf conf, final TextFactory textFactory, final TextAnalyser textAnalyser) {
        this.textFactory = textFactory;
		this.textAnalyser = textAnalyser;
        this.appendBaseIndexDir = conf.getAppendBaseIndexDir();
        this.appendToDir = conf.getAppendToDir();
	}

	public AppendDone run(final Append append) throws IOException {

		LOGGER.info("append {}", append);

        //ANALYSE
        FileInputStream input;
        try {
            input = new FileInputStream(Resolver.get(append.getSource()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Lecture fichier", e);
        }
        final Text text = textFactory.build(input);
        text.setUrl(append.getUrl());
        text.setTitle(append.getTitle());
        textAnalyser.analyse(text);

        //STORE
        final String indexDir = withSlash(appendBaseIndexDir) + forFilename(text.getTitle());
        final boolean overwrite = Files.isDirectory(Paths.get(indexDir));
        PhraseStore store = new PhraseStore(indexDir);

        store.start();
        store.store(text);
        final long phraseCount = store.stop();

        //RESPONSE
        final AppendDone appendDone = new AppendDone();
        appendDone.setSourceIndexDir(indexDir);
        appendDone.setPhraseCount(phraseCount);
        appendDone.setOverwriteOccurs(overwrite);
        appendDone.setDestIndexDir(Strings.firstValued(this.appendToDir, append.getAppendToDir()));
        return appendDone;
    }
}
