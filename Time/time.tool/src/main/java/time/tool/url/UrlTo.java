package time.tool.url;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class UrlTo {
	public static byte[] bytes(final String url) throws MalformedURLException, IOException{
		return IOUtils.toByteArray(new URL(url));
	}
}
