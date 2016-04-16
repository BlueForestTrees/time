package time.crawler.livre;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.crawler.write.IWriter;
import time.crawler.write.tika.MyTikaMetadata;
import time.tool.file.Dirs;

public class Livreman {

	private static final Logger LOGGER = LogManager.getLogger(Livreman.class);

	public IWriter writer;

	private Conf conf;
	
	final ObjectMapper mapper = new ObjectMapper();

	@Inject
	public Livreman(final IWriter writer, @Named("conf") final Conf conf) {
		this.writer = writer;
		this.conf = conf;
	}

	public void run() {
		final String sourceDir = conf.getSourceDir();
		if (sourceDir == null) {
			throw new IllegalArgumentException("invalid sourceDir: " + sourceDir);
		}
		Dirs.files(sourceDir).forEach(this::writePage);
	}

	/**
	 * Convertit une source epub/pdf en page.
	 * 
	 * @param source
	 */
	private void writePage(final File source) {
		LOGGER.info(source);
		FileInputStream input;
		try {
			input = new FileInputStream(source);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Lecture fichier", e);
		}

		toPages(input);
	}
	
	private void toPages(final InputStream input){
		final ContentHandler textHandler = new BodyContentHandler(Integer.MAX_VALUE);
		final Metadata metadata = new Metadata();
		try {
			final AutoDetectParser parser = new AutoDetectParser();
			parser.parse(input, textHandler, metadata);
			input.close();
		} catch (IOException | SAXException | TikaException e) {
			throw new RuntimeException("Parsing fichier", e);
		}

		try {
			final MyTikaMetadata metadatas = new MyTikaMetadata(metadata);
			final String datas = mapper.writeValueAsString(metadatas);
			writer.writePage("epub", metadatas.getTitle(), datas, textHandler.toString());
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Ecriture fichier", e);
		}
	}

	
}

/**
 * //TODO stocker toutes les métadonnées pour la référence. final String title =
 * metadata.get("title"); final String author = metadata.get("Author");
 */