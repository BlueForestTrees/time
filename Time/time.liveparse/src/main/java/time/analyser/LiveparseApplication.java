package time.analyser;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LiveparseApplication extends Application<LiveparseConfiguration> {
    public static void main(String[] args) throws Exception {
        new LiveparseApplication().run(args);
    }

    @Override
    public String getName() {
        return "viewer";
    }

    @Override
    public void initialize(Bootstrap<LiveparseConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(LiveparseConfiguration configuration, Environment environment) {
        environment.jersey().register(new LiveparseResource());
    }

}
