package wiki.crawler;

import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;

import wiki.util.Chrono;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MongoCrawler implements ICrawler {

	static final Logger log = Logger.getLogger(MongoCrawler.class);

	private final MongoClient mongoClient;
	private final MongoDatabase db;
	private final MongoCollection<Document> pages;
	private final MongoCollection<Document> liens;

	private long pageCount;
	private long nbPageLog = 100;

	public long getNbPageLog() {
		return nbPageLog;
	}

	public void setNbPageLog(long nbPageLog) {
		this.nbPageLog = nbPageLog;
	}

	Chrono chrono = null;

	private boolean write;

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public MongoCrawler() {
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase("pages");
		pages = db.getCollection("pages");
		liens = db.getCollection("liens");

		pageCount = 0;
		if (log.isDebugEnabled()) {
			chrono = new Chrono("Writer");
			chrono.start();
		}
	}

	public void visit(Page webpage) {
		if (webpage.getParseData() instanceof HtmlParseData) {

			if (write) {
				HtmlParseData htmlData = ((HtmlParseData) webpage.getParseData());
				final WebURL webURL = webpage.getWebURL();
				final String url = webURL.getURL();
				final short depth = webURL.getDepth();
				final String title = htmlData.getTitle();
				final Set<WebURL> webliens = htmlData.getOutgoingUrls();
				final Integer nbLiens = webliens.size();
				final String text = htmlData.getText();

				Document mongopage = new Document("url", url).append("depth", depth).append("title", title).append("nbLiens", nbLiens).append("text", text);
				pages.insertOne(mongopage);

				for (WebURL lien : webliens) {

					Object id = mongopage.get("_id");
					String urlTo = lien.getURL();
					Document mongolien = new Document("from", id).append("to", urlTo);
					liens.insertOne(mongolien);
				}
			}

			pageCount++;
			if (log.isDebugEnabled() && pageCount % nbPageLog == 0) {
				chrono.stop();
				log.debug("page#" + pageCount + ": " + chrono.toString());
				chrono.start();
			}
		}
	}

	public void end() {
		// TODO Auto-generated method stub
		
	}

}
