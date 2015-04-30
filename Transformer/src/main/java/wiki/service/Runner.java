package wiki.service;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import wiki.component.reader.FinDuScanException;
import wiki.tool.chrono.Chrono;

public class Runner {
	private static final Logger logger = LogManager.getLogger(Runner.class);

	@Autowired
	private Long pageSize;
	
	private IService service;

	public void setService(IService service) {
		this.service = service;
	}

	public void run() throws IOException {
		logger.info("run");

		long total = 0;
		long nbLoop = 0;
		Chrono chrono = new Chrono("Page");
		Chrono fullChrono = new Chrono("Full");

		fullChrono.start();
		
		service.onStart();

		try {
			do {
				nbLoop++;
				chrono.start();
				service.run(pageSize);
				total += pageSize;
				chrono.stop();
				fullChrono.stop();
				logger.info("tot[" + total + " in " + fullChrono + "] avg[" + pageSize + " in " + fullChrono.toStringDividedBy(nbLoop) + " ] cur[" + pageSize
						+ " in " + chrono + "]");
			} while (true);
		} catch (FinDuScanException e) {
			logger.info("fin du scan");
		}

		service.onEnd();
		
		logger.info("run end");
	}

}
