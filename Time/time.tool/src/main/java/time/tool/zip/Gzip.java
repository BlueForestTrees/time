package time.tool.zip;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Thanks to https://gist.github.com/yfnick/227e0c12957a329ad138
 * @author Guylaine
 *
 */
public class Gzip {

	public static byte[] compress(String data) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
		final GZIPOutputStream gzip = new GZIPOutputStream(bos);
		gzip.write(data.getBytes());
		gzip.close();
		final byte[] compressed = bos.toByteArray();
		bos.close();
		return compressed;
	}
	
	public static String decompress(byte[] compressed) throws IOException {
		final ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
		final GZIPInputStream gis = new GZIPInputStream(bis);
		final BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
		final StringBuilder sb = new StringBuilder();
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		gis.close();
		bis.close();
		return sb.toString();
	}
}
