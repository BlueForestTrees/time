package time.crawler.asis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan({ "time.conf", "time.crawler.write", "time.crawler.asis" })
public class Asis implements CommandLineRunner {

	@Autowired
	public Asisman asisman;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Asis.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		asisman.go();
	}


}
