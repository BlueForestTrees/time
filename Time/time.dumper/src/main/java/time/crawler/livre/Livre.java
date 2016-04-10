package time.crawler.livre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


@Component
@ComponentScan({ "time.conf", "time.crawler.write.file", "time.crawler.livre" })
public class Livre implements CommandLineRunner {

	@Autowired
	public Livreman livreman;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Livre.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		livreman.go();
	}

}