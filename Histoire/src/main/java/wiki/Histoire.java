package wiki;

import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import wiki.config.AppConfig;
import wiki.crawler.CrawlerGroup;
import wiki.crawler.WikiCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class Histoire {

	protected static Logger log = Logger.getLogger(Histoire.class.getName());
	
	public static void main(String[] args) throws Exception {
		
		DateTime start = DateTime.now();
		
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)){
			CrawlerGroup.crawlerGroup = (CrawlerGroup)ctx.getBean(CrawlerGroup.class);
			int crawlerCount = (Integer)ctx.getBean("crawlerCount");
			CrawlController crawlController = ctx.getBean(CrawlController.class);
			crawlController.start(WikiCrawler.class, crawlerCount);	
		}
		
		log.info(new Interval(start, DateTime.now()).toString());
	}
}
