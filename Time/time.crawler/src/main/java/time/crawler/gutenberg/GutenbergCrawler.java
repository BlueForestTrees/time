package time.crawler.gutenberg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

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

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import time.conf.Conf;
import time.crawler.BaseCrawler;
import time.crawler.write.IWriter;
import time.crawler.write.tika.MyTikaMetadata;
import time.tool.url.UrlTo;

public class GutenbergCrawler extends BaseCrawler {
	private static final Logger LOGGER = LogManager.getLogger(GutenbergCrawler.class);

	protected IWriter writer;

	final ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	public GutenbergCrawler(@Named("conf") final Conf conf, final IWriter writer) {
		super(conf);
		this.writer = writer;
	}

	@Override
	public void visit(Page page) {
		LOGGER.info(page.getWebURL().getURL());
		if (page.getParseData() instanceof HtmlParseData) {
			final Set<WebURL> outgoingUrls = ((HtmlParseData) page.getParseData()).getOutgoingUrls();
			outgoingUrls.stream().filter(url -> url.getURL().endsWith(".epub")).forEach(this::writePage);
		}
	}

	/**
	 * Convertit une source epub/pdf en page.
	 * 
	 * @param source
	 */
	private void writePage(final WebURL webURL) {
		final String url = webURL.getURL();
		byte[] bytes;
		try {
			bytes = UrlTo.bytes(url);
		} catch (IOException e) {
			throw new RuntimeException("Téléchargement fichier", e);
		}
		final InputStream input = new ByteArrayInputStream(bytes);
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