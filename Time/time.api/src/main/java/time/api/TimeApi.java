package time.api;

import static org.springframework.boot.SpringApplication.run;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import time.api.config.TimeApiConfig;


@SpringBootApplication
public class TimeApi {

    public static void main(String[] args) {
        run(TimeApiConfig.class, args);
    }

}
