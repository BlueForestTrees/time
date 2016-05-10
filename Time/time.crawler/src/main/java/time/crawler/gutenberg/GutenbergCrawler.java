package time.crawler.gutenberg;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.crawler.crawl.BaseCrawler;
import time.crawler.work.write.IWriter;
import time.repo.bean.Text;
import time.tika.ToText;

import java.io.IOException;
import java.util.Set;

public class GutenbergCrawler extends BaseCrawler {
	private static final Logger LOGGER = LogManager.getLogger(GutenbergCrawler.class);

	protected IWriter writer;
	private final ToText toText = new ToText();

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

	private void writePage(final WebURL webURL) {
		Text text;
		try {
			text = toText.fromUrl(webURL.getURL());
		} catch (IOException e) {
			throw new RuntimeException("Téléchargement fichier", e);
		}
		writer.writePage(text);
	}

}