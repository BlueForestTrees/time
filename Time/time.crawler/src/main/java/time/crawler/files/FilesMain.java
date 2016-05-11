package time.crawler.files;

import com.google.inject.Guice;

public class FilesMain {

    private FilesMain() {

    }

    public static void main(final String[] args) throws Exception {
        Guice.createInjector(new FilesModule(args)).getInstance(FilesRun.class).run();
    }

}