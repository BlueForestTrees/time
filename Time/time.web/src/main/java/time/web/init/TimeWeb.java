package time.web.init;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import time.web.config.WebConfig;

public class TimeWeb {

    public static void main(String[] args) {
        final AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);
        webContext.refresh();
    }

}
