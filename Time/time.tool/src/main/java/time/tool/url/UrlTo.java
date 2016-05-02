package time.tool.url;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class UrlTo {
	public static InputStream inputStream(final String url) throws MalformedURLException, IOException{
		return new ByteArrayInputStream(IOUtils.toByteArray(new URL(url)));
	}
}
