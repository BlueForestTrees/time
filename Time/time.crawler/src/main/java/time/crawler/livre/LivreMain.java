package time.crawler.livre;

import com.google.inject.Guice;

public class LivreMain {

    private LivreMain() {

    }

    public static void main(final String[] args) throws Exception {
        Guice.createInjector(new LivreModule(args)).getInstance(LivreRun.class).run();
    }

}