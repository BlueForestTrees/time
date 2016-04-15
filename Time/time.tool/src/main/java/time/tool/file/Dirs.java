package time.tool.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Dirs {
	
	public static Stream<File> files(final String dir) {
			return list(dir)
					    .filter(p -> java.nio.file.Files.isRegularFile(p))
					    .filter(p -> !p.toString().toLowerCase().endsWith(".conf"))
			            .map(p -> p.toFile())
			            .map(File.class::cast);
	}

	private static Stream<Path> list(final String dir) {
		try {
			if(!new File(dir).isDirectory()){
				throw new IllegalArgumentException("not a dir: " + dir);
			}
			return Files.walk(Paths.get(dir));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String filenameAble(final String title) {
		return title.replaceAll("[^a-zA-Z0-9.-]", "_");
	}
	
}
