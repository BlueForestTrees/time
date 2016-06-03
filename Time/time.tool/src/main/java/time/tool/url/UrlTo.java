package time.tool.url;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class UrlTo {
	public static byte[] byteArray(final String url) throws IOException {
		return IOUtils.toByteArray(new URL(url));
	}
}
