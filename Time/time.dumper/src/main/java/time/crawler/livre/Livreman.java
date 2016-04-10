package time.crawler.livre;

import java.io.File;
import java.io.FileInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;

import time.conf.Conf;
import time.crawler.write.IWriter;
import time.tool.file.Dirs;

@Component
public class Livreman {
	
	private static final Logger LOGGER = LogManager.getLogger(Livreman.class);
    	
	@Autowired
	public IWriter writer;
	
	@Autowired
	private Conf conf;
	
	public void go(){
		Dirs.files(conf.getSourceDir()).forEach(e -> og(e));
	}

	private void og(final File source) {
		LOGGER.info(source);
		try {
			final ContentHandler textHandler = new BodyContentHandler(Integer.MAX_VALUE);
			final Metadata metadata = new Metadata();
			final AutoDetectParser parser = new AutoDetectParser();
			FileInputStream input;
			input = new FileInputStream(source);
			parser.parse(input, textHandler, metadata);
			input.close();
			//TODO stocker toutes les métadonnées pour la référence.
			final String title = metadata.get("title");
			final String author = metadata.get("Author");
			writer.writePage("epub", title + " de " + author, textHandler.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
