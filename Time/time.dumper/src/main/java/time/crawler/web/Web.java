package time.crawler.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan({ "time.conf", "time.crawler.write.log", "time.crawler.web" })
public class Web implements CommandLineRunner {
	
	@Autowired
	public Webman wikiman;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Web.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		wikiman.go();
	}

}