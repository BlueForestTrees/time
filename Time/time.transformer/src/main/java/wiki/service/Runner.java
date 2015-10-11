package wiki.service;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.tool.chrono.Chrono;
import wiki.component.reader.FinDuScanException;

@Component
public class Runner {
	private static final Logger log = LogManager.getLogger(Runner.class);

	@Autowired
	private Long pageSize;
	@Autowired
	private long pageTotal;
	
	private FindPhrasesService service;


	public void setService(FindPhrasesService service) {
		this.service = service;
	}

	public void run() throws IOException {
		log.info("run");

		long pageCount = 0;
		long phraseCount = 0;
		Chrono chrono = new Chrono("Page");
		Chrono fullChrono = new Chrono("Full");

		fullChrono.start();
		
		service.onStart();

		try {
			do {
				chrono.start();
				phraseCount+=service.run(pageSize);
				pageCount += pageSize;
				chrono.stop();
				fullChrono.stop();
				log.debug("#" + pageCount + ", Total:"+fullChrono+", Moy:"+ fullChrono.toStringDividedBy(pageSize) +", last:" + chrono + ", reste:" + fullChrono.getRemaining(pageCount, pageTotal ) + " phrase#"+phraseCount);
			} while (true);
		} catch (FinDuScanException e) {
			log.info("fin du scan (" + pageCount + " pages)");
		}

		service.onEnd();
		
		log.info("run end");
	}

}
