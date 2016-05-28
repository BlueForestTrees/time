package time.local.tika;

import com.google.inject.Guice;

public class FilesMain {

    private FilesMain() {

    }

    public static void main(final String[] args) throws Exception {
        FilesRun instance = Guice.createInjector(new FilesModule(args)).getInstance(FilesRun.class);
        while(true) {
            instance.run();
        }
    }

}