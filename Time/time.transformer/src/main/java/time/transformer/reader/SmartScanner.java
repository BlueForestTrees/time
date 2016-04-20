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
		final String txtOutputDir = conf.getTxtOutputDir();
		final String txtOutputFile = conf.getTxtOutputFile();
		
		entries = new ArrayList<>();
		
		if(txtOutputDir != null){
			LOG.info("construction de smartScanner sur dir " + txtOutputDir);
			Path dir = FileSystems.getDefault().getPath(txtOutputDir);
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
				for (Path entry : stream) {
					addEntry(entry);
				}
			}
		}else if(txtOutputFile != null){
			LOG.info("construction de smartScanner sur file " + txtOutputFile);
			addEntry(FileSystems.getDefault().getPath(txtOutputFile));
		}else{
			throw new IllegalArgumentException("manque txtOutputDir ou txtOutputFile");
		}
		firstScanner();
		setDelimiter(Conf.getSep());
	}

	private void addEntry(Path entry) {
		if (new File(entry.toString()).isFile()) {
			LOG.info(entry.toString());
			entries.add(entry);
		} else {
			LOG.info("ignoring " + entry);
		}
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
