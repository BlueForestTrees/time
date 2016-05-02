package time.viewer;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ViewerApplication extends Application<ViewerConfiguration> {
    public static void main(String[] args) throws Exception {
        new ViewerApplication().run(args);
    }

    @Override
    public String getName() {
        return "viewer";
    }

    @Override
    public void initialize(Bootstrap<ViewerConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(ViewerConfiguration configuration, Environment environment) {
        environment.jersey().register(new ViewerResource());
    }

}
