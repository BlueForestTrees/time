package wiki.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wiki.component.reader.FinDuScanException;
import wiki.entity.Lien;
import wiki.entity.Page;
import wiki.repo.LienRepository;
import wiki.repo.PageRepository;

@Service
public class MapLiensSubService {
	private static final Logger logger = LogManager.getLogger(MapLiensSubService.class);
	
	private static final int URL_MAX_LENGTH = 255;

	@Autowired
	private String path;
	
	@Autowired
	PageRepository pageRepository;
	
	@Autowired
	LienRepository lienRepository;
	
	@Autowired
	PageReaderService pageReader;
	
	private Set<String> urlsLowerCase = new HashSet<String>();

	@Transactional
	public void run() throws IOException {
		logger.info("run with path: " + path);

		long nbPages = 0;
		long nbLiens = 0;
		long nbLiensTrouves = 0;
		
		try {
			while (true) {
				Page page = pageReader.getNextPage();
				if(isThisPageNew(page) && thisPageUrlIsnotTooLong(page)){
				Page fromPage = pageRepository.findByUrl(page.getUrl());
				
				for(String lienOut : page.getLiens()){
					
					Page toPage = pageRepository.findByUrl(lienOut);
					
					if(toPage != null){
						Long fromId = fromPage.getId();
						Long toId = toPage.getId();
						
						Lien lien = new Lien();
						lien.setFrom(fromId);
						lien.setTo(toId);
						
						lienRepository.save(lien);
						
						nbLiensTrouves++;
					}
					
					nbLiens++;
				}
				
				nbPages++;
				rememberThisPage(page);
				}
			}
		} catch (FinDuScanException e) {
			logger.info("Fin du scan, nbPages="+nbPages+"nbLiens="+nbLiens+", nbLiensTrouvés="+nbLiensTrouves);
		}
		logger.info("run end");
	}
	
	private boolean thisPageUrlIsnotTooLong(Page page) {
		return page.getUrl().length() <= URL_MAX_LENGTH;
	}

	private void rememberThisPage(Page page) {
		urlsLowerCase.add(page.getUrl().toLowerCase());
	}

	private boolean isThisPageNew(Page page) {
		return !urlsLowerCase.contains(page.getUrl().toLowerCase());
	}
}
