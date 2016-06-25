package time.web.config;

import org.eclipse.jetty.server.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan({ "time.web.controller" })
@Import(ServiceConfig.class)
public class WebConfig {

    /*@Bean
    public Server server(){
        return new Server(8080);
    }*/

}
