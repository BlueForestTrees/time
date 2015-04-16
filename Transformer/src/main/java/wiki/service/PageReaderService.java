package wiki.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.component.reader.FinDuScanException;
import wiki.component.reader.SmartScanner;
import wiki.entity.Page;

@Service
public class PageReaderService {

	private static final Logger logger = LogManager.getLogger(PageReaderService.class);
	
	@Autowired
	private String baseUrl;

	@Autowired
	private int baseUrlLength;
	
	@Autowired
	private SmartScanner scanner;
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	public Page getNextPage() throws IOException, FinDuScanException {
		String url = scanner.nextString();
		int depth = scanner.nextInt();
		String title = scanner.nextString();
		int nbLiensOut = scanner.nextInt();
		List<String> liens = new ArrayList<String>();

		if (logger.isDebugEnabled()) {
			logger.debug("url=" + url);
			logger.debug("depth=" + depth);
			logger.debug("title=" + title);
			logger.debug("nbLiensOut=" + nbLiensOut);
		}

		for (int i = 1; i <= nbLiensOut; i++) {
			String lien = scanner.nextString();
			if (lien.startsWith(baseUrl)) {
				liens.add(lien.substring(baseUrlLength));
				if (logger.isDebugEnabled()) {
					logger.debug("lien#" + i + "=" + lien);
				}
			}
		}
		String text = scanner.nextString();

		Page page = new Page();
		page.setLiens(liens);
		page.setUrl(url.substring(baseUrlLength));
		page.setDepth(depth);
		page.setNbLiensOut(nbLiensOut);

		if (logger.isDebugEnabled()) {
			logger.debug("text=" + text.substring(0, 50) + "[...]" + text.substring(text.length() - 50));
		}

		return page;
	}

}
