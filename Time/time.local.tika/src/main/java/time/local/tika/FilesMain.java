package time.local.tika;

import com.google.inject.Guice;

public class FilesMain {

    public static void main(final String[] args) throws Exception {
        Guice.createInjector(new FilesModule(args)).getInstance(FilesRun.class).run();
    }

}