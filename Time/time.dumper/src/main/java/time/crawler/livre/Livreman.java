package time.crawler.livre;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.crawler.conf.Conf;
import time.crawler.write.IWriter;
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

		final ContentHandler textHandler = new BodyContentHandler(Integer.MAX_VALUE);
		final Metadata metadata = new Metadata();

		try {
			final AutoDetectParser parser = new AutoDetectParser();
			final FileInputStream input = new FileInputStream(source);
			parser.parse(input, textHandler, metadata);
			input.close();
		} catch (IOException | SAXException | TikaException e) {
			throw new RuntimeException("Lecture fichier", e);
		}

		try {
			final MyMetadata metadatas = new MyMetadata(metadata);
			final String datas = mapper.writeValueAsString(metadatas);
			writer.writePage("epub", metadatas.getTitle(), datas, textHandler.toString());
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Ecriture fichier", e);
		}
	}

	private class MyMetadata {
		private final String identifier;
		private final String title;
		private final String creator;
		private final String created;
		private final String language;
		private final String type;
		private final String comments;
		
		public MyMetadata(final Metadata metadata) {
			identifier = metadata.get(TikaCoreProperties.IDENTIFIER);
			title = metadata.get(TikaCoreProperties.TITLE);
			creator = metadata.get(TikaCoreProperties.CREATOR);
			created = metadata.get(TikaCoreProperties.CREATED);
			language = metadata.get(TikaCoreProperties.LANGUAGE);
			type = metadata.get(TikaCoreProperties.TYPE);
			comments = metadata.get(TikaCoreProperties.COMMENTS);
		}

		public String getTitle() {
			return title;
		}

		public String getCreator() {
			return creator;
		}

		public String getCreated() {
			return created;
		}

		public String getLanguage() {
			return language;
		}

		public String getType() {
			return type;
		}

		public String getComments() {
			return comments;
		}

		public String getIdentifier() {
			return identifier;
		}
	}
}

/**
 * //TODO stocker toutes les métadonnées pour la référence. final String title =
 * metadata.get("title"); final String author = metadata.get("Author");
 */