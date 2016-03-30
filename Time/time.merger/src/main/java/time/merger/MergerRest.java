package time.merger;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import time.merger.service.MergeIndexService;

@EnableAutoConfiguration
@RestController
public class MergerRest {

    private MergeIndexService mergeIndexService = new MergeIndexService();
	private String src = "/home/slimane/time/Histoire/data/indexes";
	private String dest = "/home/slimane/time/Histoire/data/indexes/.histoires";
	
	@RequestMapping("/full")
    public void full() throws IOException {
        mergeIndexService.merge(src, dest);
    }
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MergerRest.class, args);
	}
	
}
