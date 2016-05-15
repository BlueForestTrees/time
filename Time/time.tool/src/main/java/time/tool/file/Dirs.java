package time.tool.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class Dirs {
	
	public static void renew(final File file) throws IOException{
		FileUtils.deleteDirectory(file);
		file.mkdirs();
	}

	public static void move(final String from, final String to) throws IOException {
        final File srcDir = new File(from);
        final File destDir = new File(to);
        FileUtils.deleteDirectory(destDir);
        FileUtils.moveDirectory(srcDir, destDir);
	}
	
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
		final String titleTemp = title.replaceAll("[^a-zA-Z0-9.-]", "_");
		return titleTemp.substring(0, Math.min(50, titleTemp.length()));
	}
	
}
