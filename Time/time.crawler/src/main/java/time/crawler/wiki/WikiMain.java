package time.crawler.wiki;

import com.google.inject.Guice;

public class WikiMain {

	private WikiMain() {

	}

	public static void main(final String[] args) throws Exception {
		Guice.createInjector(new WikiModule(args)).getInstance(WikiRun.class).start();
	}

}
