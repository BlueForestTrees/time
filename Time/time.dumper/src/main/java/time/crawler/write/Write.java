package time.crawler.write;

import time.conf.ConfKeys;

public class Write {
	public static StringBuilder concat(final String url, final String title, final String text) {
		final StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(ConfKeys.sep);
        sb.append(title);
        sb.append(ConfKeys.sep);
        sb.append(text);
        sb.append(ConfKeys.sep);
		return sb;
	}
}
