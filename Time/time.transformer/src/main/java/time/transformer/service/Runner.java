package time.transformer.service;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import time.tool.chrono.Chrono;
import time.transformer.component.reader.FinDuScanException;

@Component
public class Runner {
	private static final Logger LOG = LogManager.getLogger(Runner.class);

	@Autowired
	private Long pageSize;
	@Autowired
	private long pageTotal;
	
	@Autowired
	private IModule module;


	public void run() throws IOException {
		LOG.info("run");

		long pageCount = 0;
		long phraseCount = 0;
		Chrono chrono = new Chrono("Page");
		Chrono fullChrono = new Chrono("Full");

		fullChrono.start();
		
		module.onStart();

		try {
			do {
				chrono.start();
				phraseCount+=module.run(pageSize);
				pageCount += pageSize;
				chrono.stop();
				fullChrono.stop();
				LOG.debug("#" + pageCount + ", Total:"+fullChrono+", Moy:"+ fullChrono.toStringDividedBy(pageSize) +", last:" + chrono + ", reste:" + fullChrono.getRemaining(pageCount, pageTotal ) + " phrase#"+phraseCount);
			} while (true);
		} catch (FinDuScanException e) {
			LOG.info("fin du scan (" + pageCount + " pages)");
		}

		module.onEnd();
		
		LOG.info("run end");
	}

}
