package time.api.config;

import com.google.common.base.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import time.conf.ConfManager;
import time.conf.ConfEnum;
import time.domain.TimeWebConf;

import java.io.IOException;

@Configuration
@ComponentScan({ "time.api.service" })
@Import({LuceneConfig.class})
public class ServiceConfig {

    @Bean
    public TimeWebConf conf() throws IOException {
        return new ConfManager().get(ConfEnum.TIMEWEB, TimeWebConf.class);
    }

}
