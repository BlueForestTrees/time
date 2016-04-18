package time.transformer.reader;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;

public class SmartScanner {

	private static final Logger LOG = LogManager.getLogger(SmartScanner.class);

	private final List<Path> entries;
	private Scanner scanner;
	private String delimiter;
	private int scannerIndex;

	@Inject
	public SmartScanner(@Named("conf") Conf conf) throws IOException {
		final String sourcePath = conf.getTxtOutputDir();
		LOG.info("construction de smartScanner sur " + sourcePath);
		scanner = null;
		scannerIndex = 0;
		Path dir = FileSystems.getDefault().getPath(sourcePath);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			entries = new ArrayList<>();
			for (Path entry : stream) {
				if (new File(entry.toString()).isFile()) {
					LOG.info(entry.toString());
					entries.add(entry);
				} else {
					LOG.info("ignoring " + entry);
				}
			}
		}
		firstScanner();
		setDelimiter(conf.getSep());
	}

	private void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
		scanner.useDelimiter(delimiter);
	}

	public String nextString() throws IOException, FinDuScanException {
		try {
			return scanner.next();
		} catch (NoSuchElementException e) {
			nextScanner();
			return scanner.next();
		}
	}

	private void firstScanner() throws IOException {
		Path entry = entries.get(scannerIndex);
		LOG.info("going to file " + entry);
		scanner = new Scanner(entry);
		scannerIndex++;
	}

	private void nextScanner() throws IOException, FinDuScanException {
		scanner.close();
		try {
			firstScanner();
		} catch (IndexOutOfBoundsException e) {
			throw new FinDuScanException("fin du scan");
		}
		setDelimiter(delimiter);
	}
}
