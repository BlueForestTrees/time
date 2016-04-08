package time.crawler.wiki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan({ "time.conf", "time.crawler.write", "time.crawler.wiki" })
public class Wiki implements CommandLineRunner {
	
	@Autowired
	public Wikiman wikiman;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Wiki.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		wikiman.go();
	}

}
