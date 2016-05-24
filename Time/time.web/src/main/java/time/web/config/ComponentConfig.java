package time.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import time.conf.Args;
import time.conf.Conf;

import java.io.IOException;

@Configuration
@ComponentScan({ "time.web.service" })
@Import({LuceneConfig.class, ParserConfig.class})
public class ComponentConfig {

    @Bean
    public Conf conf() throws IOException {
        return new Args().toBean("${TIME_HOME}/conf/wiki.web.yml", Conf.class);
    }

}
