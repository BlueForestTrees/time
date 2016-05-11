package time.crawler.file;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class FileMain {

	private FileMain(){

	}

	public static void main(final String[] args) throws Exception {
		final Injector injector = Guice.createInjector(new FileModule(args));
		final FileRun service = injector.getInstance(FileRun.class);
		service.run();
	}

}
