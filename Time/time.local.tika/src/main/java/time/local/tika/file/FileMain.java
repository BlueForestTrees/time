package time.local.tika.file;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class FileMain {

	public static void main(final String[] args) throws Exception {
		Guice.createInjector(new FileModule(args)).getInstance(FileRun.class).run();
	}

}
