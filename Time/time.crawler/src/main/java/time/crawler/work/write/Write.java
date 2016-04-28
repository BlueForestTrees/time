package time.crawler.work.write;

import time.conf.Conf;

public class Write {
	public static StringBuilder concat(final String url, final String metadata, final String text) {
		final StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(Conf.sep);
        sb.append(metadata);
        sb.append(Conf.sep);
        sb.append(text);
        sb.append(Conf.sep);
		return sb;
	}
}
