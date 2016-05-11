package time.crawler.asis;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AsisMain {

	private AsisMain(){

	}

	public static void main(final String[] args) throws Exception {
		final Injector injector = Guice.createInjector(new AsisModule(args));
		final AsisRun service = injector.getInstance(AsisRun.class);
		service.run();
	}

}
