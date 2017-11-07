package time.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan({"time.api.rest"})
@Import(ServiceConfig.class)
public class WebConfig {

}
