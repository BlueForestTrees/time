package wiki.service;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wiki.component.reader.FinDuScanException;
import wiki.util.Chrono;

@Service
public class UrlFromFileToBaseService {
	private static final Logger logger = LogManager.getLogger(UrlFromFileToBaseService.class);

	@Autowired
	private Long nbPageLog;

	@Autowired
	UrlFromFileToBaseSubService subService;
	
	public void run() throws IOException {
		logger.info("run");

		long total = 0;
		long nbLoop = 0;
		Chrono chrono = new Chrono("Url_Base");
		Chrono fullChrono = new Chrono("Full");

		fullChrono.start();

		try {
			do {
				nbLoop++;
				chrono.start();
				subService.run(nbPageLog);
				total += nbPageLog;
				chrono.stop();
				fullChrono.stop();
				logger.info("tot[" + total + " in " + fullChrono + "] avg[" + nbPageLog + " in " + fullChrono.toStringDividedBy(nbLoop) + " ] cur[" + nbPageLog
						+ " in " + chrono + "]");
			} while (true);
		} catch (FinDuScanException e) {
			logger.info("fin du scan");
		}

		logger.info("run end");
	}
}
