package time.dumper.writer;

import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MongoWriter implements IWriter {

	static final Logger log = Logger.getLogger(MongoWriter.class);

	private final MongoClient mongoClient;
	private final MongoDatabase db;
	private final MongoCollection<Document> pages;
	private final MongoCollection<Document> liens;

	public MongoWriter() {
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase("pages");
		pages = db.getCollection("pages");
		liens = db.getCollection("liens");
	}

	@Override
	public void writePage(Page page) {
		HtmlParseData htmlData = ((HtmlParseData) page.getParseData());
		final WebURL webURL = page.getWebURL();
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

}
