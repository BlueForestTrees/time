package time.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan({"time.web.rest"})
@Import(ServiceConfig.class)
public class WebConfig {

}
