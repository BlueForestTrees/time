package wiki.factory;

import java.util.Set;

import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import wiki.crawler.WikiCrawler;
import wiki.entity.Page;

@Component
public class PageFactory {

	public Page buildPage(edu.uci.ics.crawler4j.crawler.Page page) {
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlData = ((HtmlParseData) page.getParseData());
			Page createdPage = new Page();

			final WebURL webURL = page.getWebURL();
			final String url = webURL.getURL();
			final int depth = webURL.getDepth();
			final String text = htmlData.getText();
			final String title =  htmlData.getTitle();
			final Set<WebURL> liens = htmlData.getOutgoingUrls();
			final Integer nbLiens = liens.size();

			createdPage.setUrl(url);
			createdPage.setDepth(depth);
			createdPage.setText(text);
			createdPage.setTitle(title);
			createdPage.setLiens(mapLiens(liens));
			createdPage.setNbLiens(nbLiens);
			
			return createdPage;
		} else {
			return null;
		}
	}

	private String mapLiens(Set<WebURL> liens) {
		StringBuilder sb = new StringBuilder();
		for(WebURL webURL : liens){
			sb.append(webURL.getURL());
			sb.append("\r\n");
		}
		return sb.toString();
	}

	/**
	 * Singleton pour la classe {@link WikiCrawler} qui n'est pas gérée par
	 * Spring.
	 */
	private static PageFactory inst;

	public static PageFactory get() {
		if (inst == null) {
			inst = new PageFactory();
		}
		return inst;
	}
}
