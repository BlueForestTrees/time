package time.transformer.component.reader;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SmartScanner {

    private static final Logger LOG = LogManager.getLogger(SmartScanner.class);

    private final List<Path> entries;

    private Scanner scanner;
    private String delimiter;
    private int scannerIndex;

    public SmartScanner(String path) throws IOException {
        LOG.info("construction de smartScanner sur " + path);
        scanner = null;
        scannerIndex = 0;
        Path dir = FileSystems.getDefault().getPath(path);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            entries = new ArrayList<Path>();
            for (Path entry : stream) {
                LOG.info(entry.toString());
                entries.add(entry);
            }
        }
        firstScanner();
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

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        scanner.useDelimiter(delimiter);
    }

    public String nextString() throws IOException, FinDuScanException {
        try {
            return scanner.next();
        } catch (NoSuchElementException e) {
            nextScanner();
            return  scanner.next();
        }
    }

    public int nextInt() throws IOException, FinDuScanException {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            String content = scanner.next();
            LOG.error("nextInt incorrect: " + content, e);
            throw e;
        } catch (NoSuchElementException e) {
            nextScanner();
            return scanner.nextInt();
        }
    }

}
